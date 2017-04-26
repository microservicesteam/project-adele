package com.microservicesteam.adele.event.boundary.web;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.microservicesteam.adele.event.domain.data.VenueDo;

@RepositoryRestResource(collectionResourceRel = "venues", path = "venues")
public interface VenueRepository extends JpaRepository<VenueDo, Long> {

}
