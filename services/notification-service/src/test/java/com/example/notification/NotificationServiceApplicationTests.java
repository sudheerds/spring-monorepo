package com.example.notification;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class NotificationServiceApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
        System.out.println(">>> Checking if table 'notifications' exists in the database...");
        assertDoesNotThrow(() -> {
            jdbcTemplate.execute("SELECT 1 FROM notifications");
            System.out.println(">>> Table 'notifications' exists! Flyway ran successfully! <<<");
        }, "Table 'notifications' does not exist! Flyway did NOT run.");
    }
}
