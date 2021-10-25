package ru.vzotov.cashreceipt.interfaces.accounting.facade.impl.assembler;

import ru.vzotov.cashreceipt.domain.model.PurchaseCategory;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.PurchaseCategoryDTO;
import ru.vzotov.cashreceipt.interfaces.common.assembler.AbstractAssembler;

public class PurchaseCategoryAssembler extends AbstractAssembler<PurchaseCategoryDTO, PurchaseCategory> {

    @Override
    public PurchaseCategoryDTO toDTO(PurchaseCategory model) {
        return model == null ? null : new PurchaseCategoryDTO(model.categoryId().value(), model.name(), model.parentId() == null ? null : model.parentId().value());
    }
}
