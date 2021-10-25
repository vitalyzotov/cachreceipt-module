package ru.vzotov.cashreceipt.interfaces.accounting.facade.impl.assembler;

import ru.vzotov.cashreceipt.domain.model.Item;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.ItemDTO;
import ru.vzotov.cashreceipt.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.cashreceipt.interfaces.common.assembler.MoneyDTOAssembler;

public class ItemDTOAssembler extends AbstractAssembler<ItemDTO, Item> {

    @Override
    public ItemDTO toDTO(Item item) {
        MoneyDTOAssembler moneyAssembler = new MoneyDTOAssembler();
        return item == null ? null :
                new ItemDTO(
                        item.name(),
                        moneyAssembler.toDTO(item.price()),
                        item.quantity(),
                        moneyAssembler.toDTO(item.sum()),
                        item.index(),
                        item.category() == null ? null : item.category().name());//todo: тут наверное нужно возвращать ID?
    }

}
