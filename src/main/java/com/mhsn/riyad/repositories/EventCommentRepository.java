package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.EventComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCommentRepository extends JpaRepository<EventComment, Long> {
}