package com.microservicesteam.adele;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.microservicesteam")
@EnableScheduling
public class AdeleApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdeleApplication.class, args);
    }
}
