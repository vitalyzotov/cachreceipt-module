package ru.vzotov.cashreceipt.interfaces.accounting.rest;

import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.CashreceiptsFacade;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.PurchaseCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounting/purchase-categories")
@CrossOrigin
public class PurchaseCategoriesController {
    private final CashreceiptsFacade cashreceiptsFacade;

    @Autowired
    public PurchaseCategoriesController(CashreceiptsFacade cashreceiptsFacade) {
        this.cashreceiptsFacade = cashreceiptsFacade;
    }

    @GetMapping
    public List<PurchaseCategoryDTO> listCategories() {
        return cashreceiptsFacade.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    public PurchaseCategoryDTO getCategory(@PathVariable String categoryId) {
        return cashreceiptsFacade.getCategory(new PurchaseCategoryId(categoryId));
    }

    @PostMapping
    public PurchaseCategoryDTO createNewCategory(@RequestBody PurchaseCategoryDTO category) {
        return cashreceiptsFacade.createNewCategory(category.getName());
    }

    @PatchMapping("/{categoryId}")
    public PurchaseCategoryDTO renameCategory(@PathVariable String categoryId, @RequestBody PurchaseCategoryDTO category) {
        return cashreceiptsFacade.renameCategory(new PurchaseCategoryId(categoryId), category.getName());
    }
}
