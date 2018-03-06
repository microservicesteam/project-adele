package com.microservicesteam.adele.admin.boundary.web;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.microservicesteam.adele.admin.domain.Sector;

@RepositoryRestResource(collectionResourceRel = "sectors", path = "sectors")
public interface SectorRepository extends JpaRepository<Sector, Long> {
}
