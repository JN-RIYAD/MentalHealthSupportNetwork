package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}