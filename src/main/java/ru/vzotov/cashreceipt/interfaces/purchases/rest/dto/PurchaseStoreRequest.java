package ru.vzotov.cashreceipt.interfaces.purchases.rest.dto;

import ru.vzotov.cashreceipt.interfaces.common.dto.MoneyDTO;

import java.time.LocalDateTime;

public class PurchaseStoreRequest {

    private String checkId;

    private String name;

    private LocalDateTime dateTime;

    private MoneyDTO price;

    private Double quantity;

    private String categoryId;

    private String operationId;

    public PurchaseStoreRequest() {
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public MoneyDTO getPrice() {
        return price;
    }

    public void setPrice(MoneyDTO price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
