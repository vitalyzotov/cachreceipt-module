package ru.vzotov.cashreceipt.infrastructure.persistence.jpa;

import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryRepository;
import ru.vzotov.cashreceipt.domain.model.CheckQRCodeRepository;
import ru.vzotov.cashreceipt.domain.model.CheckRepository;
import ru.vzotov.purchases.domain.model.PurchaseRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration("jpa-cashreceipts")
@EnableTransactionManagement
public class JpaConfig {

    private final DataSource dataSource;

    public JpaConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public PurchaseRepository purchaseRepository(@Qualifier("cashreceipt-emf") EntityManager em) {
        return new PurchaseRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public CheckRepository checkRepository(@Qualifier("cashreceipt-emf") EntityManager em) {
        return new CheckRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public CheckQRCodeRepository checkQRCodeRepository(@Qualifier("cashreceipt-emf") EntityManager em) {
        return new CheckQRCodeRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public PurchaseCategoryRepository purchaseCategoryRepository(@Qualifier("cashreceipt-emf") EntityManager em) {
        return new PurchaseCategoryRepositoryJpa(em);
    }

    @Bean("cashreceipt-emf")
    public LocalContainerEntityManagerFactoryBean cashreceiptEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .persistenceUnit("cashreceipts")
                .build();
    }

    @Bean("cashreceipt-tx")
    public JpaTransactionManager transactionManager(@Qualifier("cashreceipt-emf") final EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
