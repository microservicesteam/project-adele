package com.microservicesteam.adele;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.microservicesteam")
public class AdeleApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdeleApplication.class, args);
    }
}