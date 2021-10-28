package ru.vzotov.cashreceipt.interfaces.accounting.facade.impl;

import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.application.CheckItemNotFoundException;
import ru.vzotov.cashreceipt.application.CheckNotFoundException;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.CashreceiptsFacade;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.CheckDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.PurchaseCategoryDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.QRCodeDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.TimelineDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CashreceiptsFacadeImplTest {

    @Autowired
    private CashreceiptsFacade facade;

    @Test
    public void listAllChecks() {
        List<CheckDTO> checks = facade.listAllChecks(
                LocalDate.of(2018, Month.JUNE, 16), LocalDate.of(2018, Month.JUNE, 17));
        assertThat(checks).isNotEmpty();
    }

    @Test
    public void getCheck() {
        CheckDTO check = facade.getCheck("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(check).isNotNull();
        assertThat(check.getDateTime()).isEqualByComparingTo(LocalDateTime.of(2018, Month.JUNE, 16, 13, 55, 0));

        check = facade.getCheck("t=20200112T1055&s=110.00&fn=9280440300024677&i=35260&fp=1993523059&n=1");
        assertThat(check).isNotNull();

        check = facade.getCheck("t=20200112T1056&s=110.00&fn=9280440300024678&i=35261&fp=1993523060&n=1");
        assertThat(check).isNotNull();
    }

    @Test
    public void getNotExistingCheck() {
        CheckDTO check = facade.getCheck("t=20180618T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(check).isNull();
    }

    @Test
    public void listAllCodes() {
        List<QRCodeDTO> codes = facade.listAllCodes(
                LocalDate.of(2018, Month.JUNE, 16), LocalDate.of(2018, Month.JUNE, 17));
        assertThat(codes).isNotEmpty();
    }

    @Test
    public void getTimeline() {
        TimelineDTO timeline = facade.getTimeline();
        assertThat(timeline).isNotNull();
        assertThat(timeline.getPeriods()).isNotEmpty();
    }

    @Test
    public void assignCategoryToItem() throws CheckNotFoundException, CheckItemNotFoundException {
        facade.assignCategoryToItem(new CheckId("20180616135500_65624_8710000100313204_110992_2128735201_1"), 1, "Табак");
        CheckDTO check = facade.getCheck("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(check.getItems().get(1).getCategory()).isEqualTo("Табак");
    }

    @Test
    public void getAllCategories() {
        List<PurchaseCategoryDTO> categories = facade.getAllCategories();
        assertThat(categories).isNotEmpty();
    }

    @Test
    public void getCategory() {
        PurchaseCategoryDTO category = facade.getCategory(new PurchaseCategoryId("id-Табак"));
        assertThat(category.getName()).isEqualTo("Табак");
    }

    @Test
    public void createNewCategory() {
        PurchaseCategoryDTO newCategory = facade.createNewCategory("Алкоголь");
        assertThat(newCategory).isNotNull();
        PurchaseCategoryDTO persistentCategory = facade.getCategory(new PurchaseCategoryId(newCategory.getCategoryId()));
        assertThat(persistentCategory).isNotNull();
        assertThat(persistentCategory.getName()).isEqualTo(newCategory.getName());
    }

    @Test
    public void renameCategory() throws CheckNotFoundException, CheckItemNotFoundException {
        PurchaseCategoryDTO category = facade.getCategory(new PurchaseCategoryId("id-12345678901234567890"));
        facade.assignCategoryToItem(new CheckId("20180616135500_65624_8710000100313204_110992_2128735201_1"), 0, category.getName());
        facade.renameCategory(new PurchaseCategoryId(category.getCategoryId()), "Пакеты 2");
        CheckDTO check = facade.getCheck("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(check.getItems().get(0).getCategory()).isEqualTo("Пакеты 2");
    }
}
