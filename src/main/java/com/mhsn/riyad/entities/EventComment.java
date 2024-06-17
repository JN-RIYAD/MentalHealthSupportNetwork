package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "event_comment")
@NoArgsConstructor
public class EventComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_content", columnDefinition = "nvarchar(500)", nullable = false)
    private String commentContent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "comment_date_time")
    private LocalDateTime commentDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date_time", nullable = true)
    private LocalDateTime updatedDateTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    public String getCommentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return commentDateTime.toLocalDate().format(formatter);
    }

    public LocalTime getCommentTime() {
        return commentDateTime.toLocalTime();
    }

    public String getUpdatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return updatedDateTime.toLocalDate().format(formatter);
    }

    public LocalTime getUpdatedTime() {
        return updatedDateTime.toLocalTime();
    }

    @Override
    public String toString() {
        return "EventComment{" +
                "id=" + id +
                ", commentContent='" + commentContent + '\'' +
                ", event=" + event +
                ", user=" + user +
                '}';
    }

}
