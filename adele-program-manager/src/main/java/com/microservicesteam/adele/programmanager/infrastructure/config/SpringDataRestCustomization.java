package com.microservicesteam.adele.programmanager.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.microservicesteam.adele.programmanager.domain.Program;
import com.microservicesteam.adele.programmanager.domain.Sector;
import com.microservicesteam.adele.programmanager.domain.Venue;

@Configuration
public class SpringDataRestCustomization extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        config.exposeIdsFor(Program.class, Venue.class, Sector.class);
        config.getCorsRegistry()
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET")
                .allowedHeaders("*");

    }
}
