package com.microservicesteam.adele.programmanager.boundary.web;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.microservicesteam.adele.programmanager.domain.Program;

@RepositoryRestResource(collectionResourceRel = "programs", path = "programs")
public interface ProgramRepository extends JpaRepository<Program, Long> {

}