package com.raphael.pesapal_interview.config;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.integration.view.spring.EnableEntityViews;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableEntityViews("com.raphael.pesapal_interview.repository.blaze.entityViews")
public class DBConfig {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @Bean
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .schemas(new String[]{"finance"})
                .defaultSchema("finance")
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();

    }


    @Bean
    @Profile("dev")
    public CommandLineRunner dropSchemaRunnerDev(JdbcTemplate jdbcTemplate, Flyway flyway) {
        return args -> {

            jdbcTemplate.execute("DROP SCHEMA  IF EXISTS finance CASCADE");
            log.info("Finance schema dropped successfully");

            log.info("Flyway migration flyway");
            flyway.migrate();

        };

    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Lazy(value = false)
    public CriteriaBuilderFactory createCriteriaBuilderFactory(EntityManagerFactory entityManagerFactory) {
        CriteriaBuilderConfiguration config = Criteria.getDefault();
        return config.createCriteriaBuilderFactory(entityManagerFactory);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Lazy(value = false)
    public EntityViewManager createEntityViewManager(CriteriaBuilderFactory criteriaBuilderFactory, EntityViewConfiguration entityViewConfiguration) {
        return entityViewConfiguration.createEntityViewManager(criteriaBuilderFactory);
    }
}
