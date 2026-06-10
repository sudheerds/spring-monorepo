package com.example.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class OrderServiceApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
        System.out.println(">>> Checking if table 'orders' exists in the database...");
        assertDoesNotThrow(() -> {
            jdbcTemplate.execute("SELECT 1 FROM orders");
            System.out.println(">>> Table 'orders' exists! Flyway ran successfully! <<<");
        }, "Table 'orders' does not exist! Flyway did NOT run.");
    }
}
