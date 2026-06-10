package com.example.jpa.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseAutoCreator implements BeanFactoryPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String url = environment.getProperty("spring.datasource.url");
        String username = environment.getProperty("spring.datasource.username", "postgres");
        String password = environment.getProperty("spring.datasource.password", "postgres");

        if (url == null || !url.startsWith("jdbc:postgresql:")) {
            return;
        }

        try {
            createDatabaseIfNotExist(url, username, password);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to auto-create database for URL: " + url, e);
        }
    }

    private void createDatabaseIfNotExist(String url, String username, String password) throws Exception {
        // Remove prefix "jdbc:postgresql:"
        String cleanUrl = url.substring("jdbc:postgresql:".length());
        
        if (!cleanUrl.startsWith("//")) {
            return; // Not a standard host-port network URL
        }
        
        int dbNameSlashIndex = cleanUrl.indexOf('/', 2);
        if (dbNameSlashIndex == -1) {
            return; // No database name specified
        }
        
        String hostPortPart = cleanUrl.substring(0, dbNameSlashIndex);
        String dbPathAndParams = cleanUrl.substring(dbNameSlashIndex + 1);
        
        int queryParamIndex = dbPathAndParams.indexOf('?');
        String dbName = queryParamIndex == -1 ? dbPathAndParams : dbPathAndParams.substring(0, queryParamIndex);
        
        if (dbName.trim().isEmpty() || "postgres".equalsIgnoreCase(dbName)) {
            return; // Already default postgres DB
        }
        
        // Reconstruct admin url pointing to "postgres" database
        String adminUrl = "jdbc:postgresql:" + hostPortPart + "/postgres";
        if (queryParamIndex != -1) {
            adminUrl += dbPathAndParams.substring(queryParamIndex);
        }
        
        // Load driver class
        Class.forName("org.postgresql.Driver");
        
        System.out.println("[DatabaseAutoCreator] Checking if database '" + dbName + "' exists using: " + adminUrl);
        try (Connection conn = DriverManager.getConnection(adminUrl, username, password)) {
            conn.setAutoCommit(true);
            
            boolean dbExists = false;
            String checkSql = "SELECT 1 FROM pg_database WHERE datname = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, dbName);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        dbExists = true;
                    }
                }
            }
            
            if (!dbExists) {
                System.out.println("[DatabaseAutoCreator] Database '" + dbName + "' does not exist. Creating...");
                if (!dbName.matches("^[a-zA-Z0-9_-]+$")) {
                    throw new IllegalArgumentException("Invalid database name: " + dbName);
                }
                String createSql = "CREATE DATABASE \"" + dbName + "\"";
                try (Statement createStmt = conn.createStatement()) {
                    createStmt.executeUpdate(createSql);
                    System.out.println("[DatabaseAutoCreator] Database '" + dbName + "' created successfully!");
                }
            } else {
                System.out.println("[DatabaseAutoCreator] Database '" + dbName + "' already exists.");
            }
        }
    }
}
