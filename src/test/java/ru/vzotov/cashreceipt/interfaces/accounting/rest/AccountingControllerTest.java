package ru.vzotov.cashreceipt.interfaces.accounting.rest;

import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.cashreceipt.ApplicationSecurity;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.application.nalogru2.CheckRepositoryNalogru2;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.QRCodeDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.QRCodeDataDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.rest.dto.CheckRegistrationRequest;
import ru.vzotov.cashreceipt.interfaces.accounting.rest.dto.CheckRegistrationResponse;
import ru.vzotov.cashreceipt.interfaces.accounting.rest.dto.GetCheckResponse;
import ru.vzotov.cashreceipt.interfaces.accounting.rest.dto.GetChecksResponse;
import ru.vzotov.cashreceipt.interfaces.common.dto.MoneyDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.time.Month.JUNE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Import(ApplicationSecurity.class)
@Transactional
public class AccountingControllerTest {

    @MockBean
    private CheckRepositoryNalogru2 nalogru;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getChecks() {
        GetChecksResponse body = this.restTemplate.getForObject("/accounting/checks/?from=2018-06-16&to=2018-06-17", GetChecksResponse.class);
        assertThat(body.getChecks()).isNotEmpty();
    }

    @Test
    public void getCodes() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-version", "2");
        ResponseEntity<QRCodeDTO[]> response = this.restTemplate.exchange(
                "/accounting/checks/?from=2018-06-16&to=2018-06-17",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                QRCodeDTO[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        QRCodeDTO expected = new QRCodeDTO(
                "20180616135500_65624_8710000100313204_110992_2128735201_1",
                new QRCodeDataDTO(
                        LocalDateTime.of(2018, JUNE, 16, 13, 55, 0),
                        new MoneyDTO(65624, "RUR"),
                        "8710000100313204",
                        "110992",
                        "2128735201",
                        1L
                ),
                "NEW",
                1L,
                OffsetDateTime.of(LocalDateTime.of(2018, JUNE, 16, 14, 0, 0), ZoneOffset.UTC)
        );
        assertThat(response.getBody()[0]).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    public void getCheck() {
        GetCheckResponse body = this.restTemplate.getForObject("/accounting/checks/2128735201?t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&n=1", GetCheckResponse.class);
        assertThat(body.getCheck().getItems()).hasSize(2);
    }

    @Test
    public void registerCheck() throws IOException {
        final String qrValue = "t=20180717T1655&s=1350.00&fn=9288000100080483&i=944&fp=2361761706&n=1";
        final QRCodeData qr = new QRCodeData(qrValue);

        try (BufferedReader data = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/check017.json"))))) {
            String dataString = data.lines().collect(Collectors.joining(System.lineSeparator()));

            given(this.nalogru.findByQRCodeData(qr)).willReturn(dataString);

            CheckRegistrationRequest request = new CheckRegistrationRequest();
            request.setQrcode("t=20180717T1655&s=1350.00&fn=9288000100080483&i=944&fp=2361761706&n=1");
            CheckRegistrationResponse response = this.restTemplate.postForObject("/accounting/checks/", request, CheckRegistrationResponse.class);
            assertThat(response).isNotNull();
            assertThat(response.getId()).isNotNull();
            verify(this.nalogru, times(0)).findByQRCodeData(any(QRCodeData.class));
            verifyNoMoreInteractions(this.nalogru);
        }
    }
}
