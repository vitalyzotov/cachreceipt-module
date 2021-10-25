package ru.vzotov.cashreceipt.config;

import ru.vzotov.cashreceipt.application.CheckParsingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParsersConfig {

    @Bean
    public CheckParsingService nalogruParser() {
        return new ru.vzotov.cashreceipt.application.nalogru.CheckParsingService();
    }

    @Bean
    public CheckParsingService nalogru2Parser() {
        return new ru.vzotov.cashreceipt.application.nalogru2.CheckParsingService();
    }
}
