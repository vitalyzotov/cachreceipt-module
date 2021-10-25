package ru.vzotov.cashreceipt.interfaces.accounting.facade.impl;

import ru.vzotov.cashreceipt.domain.model.Check;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.CheckQRCode;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategory;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.application.CheckItemNotFoundException;
import ru.vzotov.cashreceipt.application.CheckNotFoundException;
import ru.vzotov.cashreceipt.application.CheckRegistrationService;
import ru.vzotov.cashreceipt.domain.model.CheckQRCodeRepository;
import ru.vzotov.cashreceipt.domain.model.CheckRepository;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryRepository;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.CashreceiptsFacade;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.CheckDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.PurchaseCategoryDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.QRCodeDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.TimePeriodDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.TimelineDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.impl.assembler.CheckDTOAssembler;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.impl.assembler.PurchaseCategoryAssembler;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.impl.assembler.QRCodeDTOAssembler;
import ru.vzotov.cashreceipt.interfaces.common.assembler.Assembler;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashreceiptsFacadeImpl implements CashreceiptsFacade {

    private static final Logger log = LoggerFactory.getLogger(CashreceiptsFacadeImpl.class);

    @Autowired
    private CheckRepository checkRepository;

    @Autowired
    private PurchaseCategoryRepository categoryRepository;

    @Autowired
    private CheckQRCodeRepository codeRepository;

    @Autowired
    private CheckRegistrationService checkRegistrationService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(value = "cashreceipt-tx", readOnly = true)
    public List<CheckDTO> listAllChecks(LocalDate fromDate, LocalDate toDate) {
        List<Check> checks = checkRepository.findByDate(fromDate, toDate);
        return checks.stream().map(check -> new CheckDTOAssembler().toDTO(check)).collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "cashreceipt-tx", readOnly = true)
    public List<QRCodeDTO> listAllCodes(LocalDate fromDate, LocalDate toDate) {
        List<CheckQRCode> checks = codeRepository.findByDate(fromDate, toDate);
        return checks.stream().map(check -> new QRCodeDTOAssembler().toDTO(check)).collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "cashreceipt-tx", readOnly = true)
    public TimelineDTO getTimeline() {
        final TimelineDTO result = new TimelineDTO();
        final ZoneId zoneId = ZoneId.systemDefault().normalized();
        final LocalDate today = LocalDate.now();
        final Check oldest = checkRepository.findOldest();

        if (oldest != null) {
            final LocalDate from = oldest.dateTime().atZone(zoneId).toLocalDate().withDayOfMonth(1);

            LocalDate startOfMonth = from;
            LocalDate endOfMonth;
            do {
                endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

                log.debug("Count checks from {} to {}", startOfMonth, endOfMonth);

                final long count = checkRepository.countByDate(startOfMonth, endOfMonth);
                final TimePeriodDTO period = new TimePeriodDTO(
                        startOfMonth,
                        endOfMonth,
                        count
                );
                result.getPeriods().add(period);

                startOfMonth = startOfMonth.plusMonths(1);
            } while (endOfMonth.isBefore(today));
        }

        return result;
    }

    @Override
    @Transactional(value = "cashreceipt-tx", readOnly = true)
    @Deprecated
    public CheckDTO loadCheck(String qrCodeData) {
        Check check = checkRepository.findByQRCodeData(new QRCodeData(qrCodeData));
        return check == null ? null : new CheckDTOAssembler().toDTO(check);
    }

    @Override
    @Transactional(value = "cashreceipt-tx", readOnly = true)
    public CheckDTO getCheck(String qrCodeData) {
        Check check = checkRepository.findByQRCodeData(new QRCodeData(qrCodeData));
        return check == null ? null : new CheckDTOAssembler().toDTO(check);
    }

    @Override
    @Transactional(value = "cashreceipt-tx")
    public CheckDTO loadCheckDetails(QRCodeData qrCodeData) {
        Validate.notNull(qrCodeData);
        try {
            CheckId checkId = checkRegistrationService.loadCheckDetails(qrCodeData);
            return new CheckDTOAssembler().toDTO(checkRepository.find(checkId));
        } catch (CheckNotFoundException | IOException e) {
            log.error("Unable to load check details", e);
        }
        return null;
    }

    @Override
    @Transactional(value = "cashreceipt-tx")
    public void assignCategoryToItem(CheckId checkId, Integer itemIndex, String newCategory)
            throws CheckNotFoundException, CheckItemNotFoundException {
        Check check = checkRepository.find(checkId);
        if (check == null) {
            throw new CheckNotFoundException();
        }
        PurchaseCategory newCat = categoryRepository.findByName(newCategory);

        if (check.products().items().stream().filter(item -> item.index().equals(itemIndex)).count() != 1) {
            throw new CheckItemNotFoundException();
        }

        check.assignCategoryToItem(itemIndex, newCat);
        checkRepository.store(check);
    }

    @Override
    @Transactional(value = "cashreceipt-tx", readOnly = true)
    public List<PurchaseCategoryDTO> getAllCategories() {
        Assembler<PurchaseCategoryDTO, PurchaseCategory> assembler = new PurchaseCategoryAssembler();
        return assembler.toDTOList(categoryRepository.findAll());
    }

    @Override
    @Transactional(value = "cashreceipt-tx", readOnly = true)
    public PurchaseCategoryDTO getCategory(PurchaseCategoryId id) {
        Assembler<PurchaseCategoryDTO, PurchaseCategory> assembler = new PurchaseCategoryAssembler();
        return assembler.toDTO(categoryRepository.findById(id));
    }

    @Override
    @Transactional(value = "cashreceipt-tx")
    public PurchaseCategoryDTO createNewCategory(String name) {
        Assembler<PurchaseCategoryDTO, PurchaseCategory> assembler = new PurchaseCategoryAssembler();
        PurchaseCategory category = new PurchaseCategory(PurchaseCategoryId.nextId(), name);
        categoryRepository.store(category);
        return assembler.toDTO(category);
    }

    @Override
    @Transactional(value = "cashreceipt-tx")
    public PurchaseCategoryDTO renameCategory(PurchaseCategoryId id, String newName) {
        Assembler<PurchaseCategoryDTO, PurchaseCategory> assembler = new PurchaseCategoryAssembler();
        PurchaseCategory category = categoryRepository.findById(id);
        category.rename(newName);
        categoryRepository.store(category);
        return assembler.toDTO(category);
    }

}
