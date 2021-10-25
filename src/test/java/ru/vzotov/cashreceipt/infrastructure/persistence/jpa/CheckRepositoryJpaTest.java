package ru.vzotov.cashreceipt.infrastructure.persistence.jpa;

import ru.vzotov.cashreceipt.domain.model.Check;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategory;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.CheckFactory;
import ru.vzotov.cashreceipt.config.DatasourceConfig;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryRepository;
import ru.vzotov.cashreceipt.domain.model.CheckRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@Import({DatasourceConfig.class, JpaConfig.class})
public class CheckRepositoryJpaTest {

    private static final Logger log = LoggerFactory.getLogger(CheckRepositoryJpaTest.class);

    @Autowired
    private CheckRepository checkRepository;

    @Autowired
    private PurchaseCategoryRepository categoryRepository;

    @Test
    public void find() {
        Check check = checkRepository.find(new CheckId("20180616135500_65624_8710000100313204_110992_2128735201_1"));
        log.info("Check loaded {}", check);
        Assert.assertNotNull(check);
    }

    @Test
    public void assignItemToCategory() {
        Check check = checkRepository.find(new CheckId("20180616135500_65624_8710000100313204_110992_2128735201_1"));
        log.info("Check loaded {}", check);
        Assert.assertNotNull(check);

        PurchaseCategory cat = categoryRepository.findByName("Пакеты");
        log.info("Category loaded {}", cat);
        Assert.assertNotNull(cat);

        check.assignCategoryToItem(0, cat);

        checkRepository.store(check);
        log.info("Check persisted {}", check);
    }

    @Test
    public void store() {
        Check check = new CheckFactory().createCheckSimple();
        checkRepository.store(check);
    }

    @Test
    public void storeLongUser() {
        Check check = new CheckFactory().createCheckWithLongUser();
        checkRepository.store(check);
    }

    @Test
    public void findByQRCodeData() {
        Check notFound = checkRepository.findByQRCodeData(new QRCodeData("t=20180614T1641&s=566.92&fn=8710000100312991&i=21128&fp=2663320648&n=1"));
        Assert.assertNull(notFound);

        Check check = checkRepository.findByQRCodeData(new QRCodeData("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1"));
        log.info("Check loaded {}", check);
        Assert.assertNotNull(check);
    }
}
