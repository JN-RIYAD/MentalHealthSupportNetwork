package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "user_therapist_message")
@NoArgsConstructor
public class UserTherapistMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    public String getSentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return sentAt.toLocalDate().format(formatter);
    }

    public LocalTime getSentTime() {
        return sentAt.toLocalTime();
    }
}

