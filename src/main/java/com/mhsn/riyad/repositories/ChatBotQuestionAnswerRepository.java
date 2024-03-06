package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.ChatBotQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatBotQuestionAnswerRepository extends JpaRepository<ChatBotQuestionAnswer, Long> {
}