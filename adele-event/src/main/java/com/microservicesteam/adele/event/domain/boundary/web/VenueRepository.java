package com.microservicesteam.adele.event.domain.boundary.web;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.microservicesteam.adele.event.domain.domain.data.VenueDo;

@RepositoryRestResource(collectionResourceRel = "venues", path = "venues")
public interface VenueRepository extends JpaRepository<VenueDo, Long> {

}
