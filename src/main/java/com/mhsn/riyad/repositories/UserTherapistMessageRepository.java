package com.mhsn.riyad.repositories;

import com.mhsn.riyad.entities.UserTherapistMessage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserTherapistMessageRepository extends JpaRepository<UserTherapistMessage, Long> {

    @Query(value = """
            SELECT utm.* FROM user_therapist_message AS utm
            WHERE
            (
                (utm.sender_id = :senderId AND utm.receiver_id = :receiverId) OR
                (utm.receiver_id = :senderId AND utm.sender_id = :receiverId)
            )
            """,
            nativeQuery = true)
    List<UserTherapistMessage> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    @Modifying
    @Query(value = """
                DELETE FROM user_therapist_message
                WHERE sender_id = :senderId AND receiver_id = :receiverId
            """,
            nativeQuery = true)
    @Transactional
    void clearMessagesBySenderIdAndReceiverId(Long senderId, Long receiverId);
}