package ru.vzotov.cashreceipt.interfaces.accounting.rest;

import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.application.CheckItemNotFoundException;
import ru.vzotov.cashreceipt.application.CheckNotFoundException;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.CashreceiptsFacade;
import ru.vzotov.cashreceipt.interfaces.accounting.rest.dto.CheckItemCategoryPatch;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounting/check-items")
@CrossOrigin
public class CheckItemsController {
    private final CashreceiptsFacade cashreceiptsFacade;

    @Autowired
    public CheckItemsController(CashreceiptsFacade cashreceiptsFacade) {
        this.cashreceiptsFacade = cashreceiptsFacade;
    }

    @PatchMapping("/{itemIndex}")
    public void patchItemCategory(@PathVariable Integer itemIndex,
                                  @RequestParam("check") String checkId,
                                  @RequestBody CheckItemCategoryPatch patch) throws CheckNotFoundException, CheckItemNotFoundException {
        Validate.notNull(itemIndex);
        Validate.isTrue(itemIndex >= 0);
        cashreceiptsFacade.assignCategoryToItem(new CheckId(checkId), itemIndex, patch.getCategory());
    }
}
