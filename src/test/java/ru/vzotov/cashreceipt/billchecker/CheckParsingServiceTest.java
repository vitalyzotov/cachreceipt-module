package ru.vzotov.cashreceipt.billchecker;

import ru.vzotov.cashreceipt.domain.model.Check;
import ru.vzotov.cashreceipt.CheckFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@RunWith(JUnit4.class)
public class CheckParsingServiceTest {
    private static final Logger log = LoggerFactory.getLogger(CheckParsingServiceTest.class);

    @Test
    public void parseMagnit() throws IOException {
        Check check = new CheckFactory().createCheckFromBillchecker("/check002.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parseSemeiny() throws IOException {
        Check check = new CheckFactory().createCheckFromBillchecker("/check004.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parseMailru() throws IOException {
        Check check = new CheckFactory().createCheckFromBillchecker("/check005.json");
        log.info("Parsed check {}", check);
    }

}
