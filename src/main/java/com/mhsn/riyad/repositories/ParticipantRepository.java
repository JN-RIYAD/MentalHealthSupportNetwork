package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.Event;
import com.mhsn.riyad.entities.Participant;
import com.mhsn.riyad.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByUserAndEvent(User user, Event event);
}