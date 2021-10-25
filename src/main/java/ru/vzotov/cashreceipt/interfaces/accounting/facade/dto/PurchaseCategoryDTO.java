package ru.vzotov.cashreceipt.interfaces.accounting.facade.dto;

public class PurchaseCategoryDTO {

    private String categoryId;

    private String name;

    private String parentId;

    public PurchaseCategoryDTO() {
    }

    public PurchaseCategoryDTO(String categoryId, String name) {
        this(categoryId, name, null);
    }

    public PurchaseCategoryDTO(String categoryId, String name, String parentId) {
        this.categoryId = categoryId;
        this.name = name;
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
