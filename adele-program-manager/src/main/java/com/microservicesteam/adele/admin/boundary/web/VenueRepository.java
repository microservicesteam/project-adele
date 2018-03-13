package com.microservicesteam.adele.admin.boundary.web;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.microservicesteam.adele.admin.domain.Venue;

@RepositoryRestResource(collectionResourceRel = "venues", path = "venues")
public interface VenueRepository extends JpaRepository<Venue, Long> {

}
