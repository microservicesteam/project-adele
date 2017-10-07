package com.microservicesteam.adele.event.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.microservicesteam.adele.event.domain.data.EventDo;
import com.microservicesteam.adele.event.domain.data.SectorDo;
import com.microservicesteam.adele.event.domain.data.VenueDo;

@Configuration
public class SpringDataRestCustomization extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        config.exposeIdsFor(EventDo.class, VenueDo.class, SectorDo.class);

    }
}
