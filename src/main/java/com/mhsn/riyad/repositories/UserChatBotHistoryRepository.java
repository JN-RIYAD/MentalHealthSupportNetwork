package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.UserChatBotHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChatBotHistoryRepository extends JpaRepository<UserChatBotHistory, Long> {
    List<UserChatBotHistory> findByUserIdOrderByIdDesc(Long id);
}