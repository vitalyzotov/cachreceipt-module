package ru.vzotov.cashreceipt.application.impl;

import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.application.CheckNotFoundException;
import ru.vzotov.cashreceipt.application.CheckRegistrationService;
import ru.vzotov.cashreceipt.application.nalogru2.CheckRepositoryNalogru2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckRegistrationServiceImplTest {
    private static final Logger log = LoggerFactory.getLogger(CheckRegistrationServiceImplTest.class);

    @MockBean
    private CheckRepositoryNalogru2 nalogru;

    @Autowired
    private CheckRegistrationService service;

    @Test
    public void registerCheck() throws CheckNotFoundException, IOException {
        final QRCodeData qr = new QRCodeData("t=20180629T002100&s=1109.00&fn=9286000100174236&i=6079&fp=3461764748&n=1");
        final QRCodeData qrNonExistent = new QRCodeData("t=20180717T1708&s=150.00&fn=9288000100080294&i=2056&fp=2024263777&n=2");
        try (BufferedReader data = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/check010.json")))) {
            String dataString = data.lines().collect(Collectors.joining(System.lineSeparator()));

            given(this.nalogru.findByQRCodeData(qr)).willReturn(dataString);
            given(this.nalogru.findByQRCodeData(qrNonExistent)).willReturn(null);

            // первичная регистрация
            CheckId checkId = service.registerCheck(qr);
            assertThat(checkId).isNotNull();

            // повторная регистрация
            CheckId checkIdRepeat = service.registerCheck(qr);
            assertThat(checkId).isEqualTo(checkIdRepeat);

            // регистрация чека, сведения по которому отсутствуют в налоговой
            service.registerCheck(qrNonExistent);

            verifyZeroInteractions(this.nalogru);
        }
    }
}
