package ru.vzotov.cashreceipt.interfaces.accounting.rest.dto;

public class CheckItemCategoryPatch {
    private String category;

    public CheckItemCategoryPatch() {
    }

    public CheckItemCategoryPatch(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
