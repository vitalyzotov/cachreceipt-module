package ru.vzotov.cashreceipt.nalogru;

import ru.vzotov.cashreceipt.domain.model.Check;
import ru.vzotov.cashreceipt.CheckFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class CheckParsingServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CheckParsingServiceTest.class);

    @Test
    public void parse003() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check003.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse006() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check006.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse007() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check007.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse008() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check008.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse009() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check009.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse010() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check010.json");
        log.info("Parsed check {}", check);

        Assert.assertEquals("user not trimmed", 14, check.retailPlace().user().length());
        Assert.assertEquals("kkt reg id not trimmed", 16, check.fiscalInfo().kktRegId().length());

    }

    @Test
    public void parse011() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check011.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse012() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check012.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse013() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check013.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse014() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check014.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse015() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check015.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse017() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check017.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse019() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check019.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse020() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check020.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse021() throws IOException {
        Check check = new CheckFactory().createCheckFromJson("/check021.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse022() throws IOException {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
            new CheckFactory().createCheckFromJson("/check022.json");
        }).isInstanceOf(Exception.class);
    }

}
