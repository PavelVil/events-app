package com.github.pavelvil.eventsapi.repository;

import com.github.pavelvil.eventsapi.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
