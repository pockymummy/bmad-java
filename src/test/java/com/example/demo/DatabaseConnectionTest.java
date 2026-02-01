package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseConnectionTest extends BaseIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void shouldConnectToSqlServer() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.isValid(5)).isTrue();
        }
    }

    @Test
    void shouldExecuteQueryOnSqlServer() throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT 1 AS test")) {

            assertThat(resultSet.next()).isTrue();
            assertThat(resultSet.getInt("test")).isEqualTo(1);
        }
    }

    @Test
    void shouldVerifySqlServerVersion() throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT @@VERSION AS version")) {

            assertThat(resultSet.next()).isTrue();
            String version = resultSet.getString("version");
            assertThat(version).containsIgnoringCase("Microsoft SQL Server");
        }
    }
}
