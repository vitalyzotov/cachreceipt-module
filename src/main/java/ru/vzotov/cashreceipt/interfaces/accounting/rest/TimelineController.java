package ru.vzotov.cashreceipt.interfaces.accounting.rest;

import ru.vzotov.cashreceipt.interfaces.accounting.facade.CashreceiptsFacade;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.TimelineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timeline")
@CrossOrigin
public class TimelineController {

    private final CashreceiptsFacade cashreceiptsFacade;

    @Autowired
    public TimelineController(CashreceiptsFacade cashreceiptsFacade) {
        this.cashreceiptsFacade = cashreceiptsFacade;
    }

    @GetMapping
    public TimelineDTO getTimeline() {
        return cashreceiptsFacade.getTimeline();
    }

}
