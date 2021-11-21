package ru.vzotov.cashreceipt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring-boot приложение, используемое юнит-тестами для своей работы
 */
@SpringBootApplication(
        scanBasePackages = {
                "ru.vzotov.cashreceipt.interfaces",
                "ru.vzotov.cashreceipt.application",
                "ru.vzotov.cashreceipt.application.impl"}
)
@EnableTransactionManagement
@EnableAspectJAutoProxy
@Import({CashreceiptsModule.class})
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }


}
