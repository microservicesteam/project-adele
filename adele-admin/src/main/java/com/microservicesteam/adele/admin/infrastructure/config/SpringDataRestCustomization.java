package com.microservicesteam.adele.admin.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.microservicesteam.adele.admin.domain.Event;
import com.microservicesteam.adele.admin.domain.Sector;
import com.microservicesteam.adele.admin.domain.Venue;

@Configuration
public class SpringDataRestCustomization extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        config.exposeIdsFor(Event.class, Venue.class, Sector.class);
        config.getCorsRegistry()
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET")
                .allowedHeaders("*");

    }
}
