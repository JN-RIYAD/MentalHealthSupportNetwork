package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.NotAnsweredQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotAnsweredQuestionRepository extends JpaRepository<NotAnsweredQuestion, Long> {
}