package ru.vzotov.cashreceipt.interfaces.accounting.rest;

import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.cashreceipt.ApplicationSecurity;
import ru.vzotov.cashreceipt.interfaces.accounting.rest.dto.CheckItemCategoryPatch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Import(ApplicationSecurity.class)
@Transactional
public class CheckItemsControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    public void patchItemCategory() {
        HttpEntity<CheckItemCategoryPatch> request = new HttpEntity<>(new CheckItemCategoryPatch("Табак"));
        ResponseEntity<Void> response = this.restTemplate.exchange("/accounting/check-items/1?check=20180616135500_65624_8710000100313204_110992_2128735201_1", HttpMethod.PATCH, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
