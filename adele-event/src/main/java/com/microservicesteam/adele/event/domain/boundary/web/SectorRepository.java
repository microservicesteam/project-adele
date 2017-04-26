package com.microservicesteam.adele.event.domain.boundary.web;

import com.microservicesteam.adele.event.domain.domain.data.SectorDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "sectors", path = "sectors")
public interface SectorRepository extends JpaRepository<SectorDo, Long> {
}
