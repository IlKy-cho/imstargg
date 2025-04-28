package com.imstargg.storage.db.core.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class CoreDatasourceConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "storage.datasource.core")
    HikariDataSource coreDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}
