package com.example.stormgate_cart_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for Stormgate Cart Service application.
 * This is a microservice responsible for managing shopping carts
 * in the Stormgate multi-tenant e-commerce platform.
 */
@SpringBootApplication
public class StormgateCartServiceApplication {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(StormgateCartServiceApplication.class, args);
    }
}

