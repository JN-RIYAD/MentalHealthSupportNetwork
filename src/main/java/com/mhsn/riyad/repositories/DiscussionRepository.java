package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
}