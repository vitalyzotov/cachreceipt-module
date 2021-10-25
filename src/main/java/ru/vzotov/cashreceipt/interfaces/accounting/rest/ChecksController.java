package ru.vzotov.cashreceipt.interfaces.accounting.rest;

import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.application.CheckNotFoundException;
import ru.vzotov.cashreceipt.application.CheckRegistrationService;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.CashreceiptsFacade;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.CheckDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.facade.dto.QRCodeDTO;
import ru.vzotov.cashreceipt.interfaces.accounting.rest.dto.CheckRegistrationRequest;
import ru.vzotov.cashreceipt.interfaces.accounting.rest.dto.CheckRegistrationResponse;
import ru.vzotov.cashreceipt.interfaces.accounting.rest.dto.GetCheckResponse;
import ru.vzotov.cashreceipt.interfaces.accounting.rest.dto.GetChecksResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounting/checks/")
@CrossOrigin
public class ChecksController {

    private final CheckRegistrationService checkRegistrationService;

    private final CashreceiptsFacade cashreceiptsFacade;

    @Autowired
    public ChecksController(CheckRegistrationService checkRegistrationService, CashreceiptsFacade cashreceiptsFacade) {
        this.checkRegistrationService = checkRegistrationService;
        this.cashreceiptsFacade = cashreceiptsFacade;
    }

    @PostMapping
    public CheckRegistrationResponse registerCheck(@RequestBody CheckRegistrationRequest request) throws CheckNotFoundException, IOException {
        final CheckId qrId = checkRegistrationService.registerCheck(new QRCodeData(request.getQrcode()));
        return new CheckRegistrationResponse(qrId.value());
    }

    @GetMapping
    public GetChecksResponse getChecks(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        final List<CheckDTO> checks = cashreceiptsFacade.listAllChecks(from, to);
        return new GetChecksResponse(checks);
    }

    @GetMapping(headers = "x-api-version=2")
    public List<QRCodeDTO> getCodes(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return cashreceiptsFacade.listAllCodes(from, to);
    }

    @GetMapping("{fiscalSign}")
    public GetCheckResponse getCheck(@PathVariable String fiscalSign, @RequestParam("t") String date,
                                     @RequestParam("s") String sum,
                                     @RequestParam("fn") String fiscalDriveNumber,
                                     @RequestParam("i") String fiscalDocumentNumber,
                                     @RequestParam("n") String operationType) throws CheckNotFoundException {
        final String queryString = getCheckQrString(fiscalSign, date, sum, fiscalDriveNumber, fiscalDocumentNumber, operationType);
        final CheckDTO check = cashreceiptsFacade.getCheck(queryString);
        if (check == null) throw new CheckNotFoundException();
        return new GetCheckResponse(check);
    }

    @PutMapping("{fiscalSign}")
    public GetCheckResponse loadCheckDetails(@PathVariable String fiscalSign, @RequestParam("t") String date,
                                             @RequestParam("s") String sum,
                                             @RequestParam("fn") String fiscalDriveNumber,
                                             @RequestParam("i") String fiscalDocumentNumber,
                                             @RequestParam("n") String operationType) throws CheckNotFoundException {
        final String queryString = getCheckQrString(fiscalSign, date, sum, fiscalDriveNumber, fiscalDocumentNumber, operationType);
        final CheckDTO check = cashreceiptsFacade.loadCheckDetails(new QRCodeData(queryString));
        if (check == null) throw new CheckNotFoundException();
        return new GetCheckResponse(check);
    }

    private String getCheckQrString(String fiscalSign, String date, String sum, String fiscalDriveNumber, String fiscalDocumentNumber, String operationType) {
        final Map<String, String> query = new HashMap<>();
        query.put("t", date);
        query.put("s", sum);
        query.put("fn", fiscalDriveNumber);
        query.put("i", fiscalDocumentNumber);
        query.put("fp", fiscalSign);
        query.put("n", operationType);
        return query.entrySet().stream()
                .map(e -> String.join("=", e.getKey(), e.getValue())).collect(Collectors.joining("&"));
    }

}
