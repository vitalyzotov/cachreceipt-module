package ru.vzotov.cashreceipt;

import ru.vzotov.cashreceipt.config.DatasourceConfig;
import ru.vzotov.cashreceipt.config.ParsersConfig;
import ru.vzotov.cashreceipt.infrastructure.persistence.jpa.JpaConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DatasourceConfig.class, ParsersConfig.class, JpaConfig.class})
public class CashreceiptsModule {

}
