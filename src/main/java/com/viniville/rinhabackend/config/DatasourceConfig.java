package com.viniville.rinhabackend.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class DatasourceConfig {

    //isto faz com que o pool de conexoes com o banco seja
    // inicializado quando a aplicacao inicia
    @Bean
    public ApplicationRunner validateDataSource(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) { }
        };
    }
}
