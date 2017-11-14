package com.microservicesteam.adele.event.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.microservicesteam.adele.event.domain.Event;
import com.microservicesteam.adele.event.domain.Sector;
import com.microservicesteam.adele.event.domain.Venue;

@Configuration
public class SpringDataRestCustomization extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        config.exposeIdsFor(Event.class, Venue.class, Sector.class);

    }
}
