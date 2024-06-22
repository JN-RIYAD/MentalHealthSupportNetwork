package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.PaymentHistory;
import com.mhsn.riyad.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    List<PaymentHistory> findByUser(User user, Sort id);
}