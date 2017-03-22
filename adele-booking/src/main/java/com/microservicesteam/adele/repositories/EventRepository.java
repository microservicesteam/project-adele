package com.microservicesteam.adele.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.microservicesteam.adele.model.data.EventDo;

@RepositoryRestResource(collectionResourceRel = "events", path = "events")
public interface EventRepository extends JpaRepository<EventDo, Long> {

}