package com.github.pavelvil.eventsapi.repository;

import com.github.pavelvil.eventsapi.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @Override
    @EntityGraph(attributePaths = {"categories", "location", "organization", "schedule"})
    Page<Event> findAll(Specification<Event> spec, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"categories", "location", "organization", "schedule"})
    List<Event> findAll();

    @Override
    @EntityGraph(attributePaths = {"categories", "location", "organization", "schedule"})
    Optional<Event> findById(Long id);

    boolean existsByIdAndParticipantsId(Long eventId, Long userId);

    boolean existsByIdAndOrganizationOwnerId(Long eventId, Long userId);

}
