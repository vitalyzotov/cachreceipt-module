package ru.vzotov.cashreceipt.application.impl;

import ru.vzotov.cashreceipt.domain.model.Check;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.CheckQRCode;
import ru.vzotov.cashreceipt.domain.model.CheckState;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.application.CheckNotFoundException;
import ru.vzotov.cashreceipt.application.CheckParsingService;
import ru.vzotov.cashreceipt.application.CheckRegistrationService;
import ru.vzotov.cashreceipt.application.nalogru2.CheckRepositoryNalogru2;
import ru.vzotov.cashreceipt.domain.model.CheckQRCodeRepository;
import ru.vzotov.cashreceipt.domain.model.CheckRepository;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional("cashreceipt-tx")
public class CheckRegistrationServiceImpl implements CheckRegistrationService {

    private static final Logger log = LoggerFactory.getLogger(CheckRegistrationServiceImpl.class);
    private static final int MAX_LOAD_SIZE = 10;

    /**
     * Максимальная частота попыток загрузки чека в часах.
     * Чек не будет отправляться на загрузку чаще чем указанное значение
     */
    private static final int LOADING_FREQUENCY_MAX = 12;

    /**
     * Репозиторий загруженных чеков.
     * Позволяет работать с чеками в БД.
     */
    @Autowired
    private CheckRepository checkRepository;

    /**
     * Репозиторий зарегистрированных QR кодов.
     * Позволяет работать с QR кодами в БД.
     */
    @Autowired
    private CheckQRCodeRepository qrCodeRepository;

    /**
     * Сервис парсинга данных чека.
     */
    @Autowired
    @Qualifier("nalogru2Parser")
    private CheckParsingService checkParsingService;

    @Autowired
    private CheckRepositoryNalogru2 nalogru;

    private long maxTryCount = 250;

    private CheckSelectionStrategy selectionStrategy = new RandomSelectionStrategy();

    @Override
    public CheckId registerCheck(QRCodeData qrCodeData) throws CheckNotFoundException, IOException {
        log.debug("Try registering check {}", qrCodeData);

        final CheckQRCode alreadyRegistered = qrCodeRepository.findByQRCodeData(qrCodeData);
        if (alreadyRegistered != null) {
            return alreadyRegistered.checkId();
        }

        final CheckQRCode qrCode = new CheckQRCode(qrCodeData);
        qrCodeRepository.store(qrCode);

        return qrCode.checkId();
    }

    @Override
    public CheckId loadCheckDetails(QRCodeData qrCodeData) throws CheckNotFoundException, IOException {
        log.debug("Try loading check details {}", qrCodeData);

        Check check = checkRepository.findByQRCodeData(qrCodeData);
        if (check != null) {
            return check.checkId();
        }

        CheckQRCode qrCode = qrCodeRepository.findByQRCodeData(qrCodeData);
        if (qrCode == null) {
            throw new CheckNotFoundException("QR code is not yet registered");
        }
        Validate.isTrue(CheckState.NEW.equals(qrCode.state()), "Wrong state of check");

        qrCode.tryLoading();
        try {
            try {
                final String checkData = nalogru.findByQRCodeData(qrCodeData);
                if (checkData == null) {
                    throw new CheckNotFoundException();
                }

                check = checkParsingService.parse(checkData);
                if (check == null) {
                    throw new CheckNotFoundException();
                }

                //check.associateWithOperations(qrCode.operations());
                checkRepository.store(check);

                qrCode.markLoaded();
            } catch (IllegalArgumentException ex) {
                log.error("Parse error when parsing check {}", qrCodeData);
                log.error("Parse error", ex);
                throw ex;
            }
        } finally {
            qrCodeRepository.store(qrCode);
        }

        return check.checkId();
    }

    private Set<CheckQRCode> selectCodesForLoading(final Collection<CheckQRCode> codes) {
        return selectionStrategy.select(codes, MAX_LOAD_SIZE);
    }

    @Override
    public void loadNewChecks() {
        log.info("Start loading new checks");

        final OffsetDateTime threshold = OffsetDateTime.now().minusHours(LOADING_FREQUENCY_MAX);
        log.info("Loading threshold {}", threshold);

        final List<CheckQRCode> allNewCodes = qrCodeRepository.findAllInState(CheckState.NEW).stream()
                // Выбираем только те чеки, которые пытались загрузить раньше порогового интервала,
                // или те которые никогда не пытались загрузить
                .filter((code) -> (code.loadedAt() == null || code.loadedAt().isBefore(threshold)) && (code.loadingTryCount() < maxTryCount))
                .collect(Collectors.toList());
        log.info("Found {} new checks", allNewCodes.size());

        if (allNewCodes.isEmpty()) {
            log.info("There are no suitable new codes. Skip loading.");
        } else {
            final Set<CheckQRCode> codesToLoad = selectCodesForLoading(allNewCodes);

            for (CheckQRCode code : codesToLoad) {
                try {
                    loadCheckDetails(code.code());
                } catch (CheckNotFoundException e) {
                    log.error("Unable to load check {} details, check was not found", code);
                } catch (IOException e) {
                    log.error("Unable to load check {} details, I/O error", code);
                }

                waitToPreventFloodLock();
            }
        }
        log.info("Finish loading new checks");
    }

    private void waitToPreventFloodLock() {
        try {
            log.info("Wait 5 seconds between subsequent calls");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            log.error("Sleep interrupt", e);
        }
    }

}
