package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.ChatBotQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatBotQuestionAnswerRepository extends JpaRepository<ChatBotQuestionAnswer, Long> {

}