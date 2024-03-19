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

    @Query(value = """
            SELECT utm.* FROM user_therapist_message AS utm
            WHERE
            utm.sender_id = :userId OR
            utm.receiver_id = :userId
            """,
            nativeQuery = true)
    List<UserTherapistMessage> findByUserId(Long userId);

    @Query(value = """
            SELECT utm.*
            FROM user_therapist_message AS utm
            JOIN (
                SELECT
                    CASE
                        WHEN utm.sender_id = :userId THEN utm.receiver_id
                        ELSE utm.sender_id
                    END AS therapist_id,
                    MAX(utm.sent_at) AS max_sent_at
                FROM user_therapist_message AS utm
                WHERE utm.sender_id = :userId OR utm.receiver_id = :userId
                GROUP BY therapist_id
            ) AS max_dates
            ON (utm.sender_id = :userId AND utm.receiver_id = max_dates.therapist_id)
                OR (utm.receiver_id = :userId AND utm.sender_id = max_dates.therapist_id)
                AND utm.sent_at = max_dates.max_sent_at;
            """,
            nativeQuery = true)
    List<UserTherapistMessage> getLastMessageListByUserId(Long userId);

    @Query(value = """
            SELECT utm.*
            FROM user_therapist_message AS utm
            JOIN (
                SELECT\s
                    CASE
                        WHEN utm.sender_id = :therapistId THEN utm.receiver_id
                        ELSE utm.sender_id
                    END AS user_id,
                    MAX(utm.sent_at) AS max_sent_at
                FROM user_therapist_message AS utm
                WHERE utm.sender_id = :therapistId OR utm.receiver_id = :therapistId
                GROUP BY user_id
            ) AS max_dates
            ON (utm.sender_id = max_dates.user_id AND utm.receiver_id = :therapistId)
                OR (utm.receiver_id = max_dates.user_id AND utm.sender_id = :therapistId)
                AND utm.sent_at = max_dates.max_sent_at;
            """,
            nativeQuery = true)
    List<UserTherapistMessage> getLastMessageListByTherapistId(Long therapistId);
}