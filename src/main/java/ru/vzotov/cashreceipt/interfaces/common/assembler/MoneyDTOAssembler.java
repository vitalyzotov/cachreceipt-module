package ru.vzotov.cashreceipt.interfaces.common.assembler;

import ru.vzotov.cashreceipt.interfaces.common.dto.MoneyDTO;
import ru.vzotov.domain.model.Money;

public class MoneyDTOAssembler extends AbstractAssembler<MoneyDTO, Money> {
    @Override
    public MoneyDTO toDTO(Money money) {
        return money == null ? null : new MoneyDTO(money.rawAmount(), money.currency().getCurrencyCode());
    }
}
