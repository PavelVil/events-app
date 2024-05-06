package com.github.pavelvil.eventsapi.repository;

import com.github.pavelvil.eventsapi.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
