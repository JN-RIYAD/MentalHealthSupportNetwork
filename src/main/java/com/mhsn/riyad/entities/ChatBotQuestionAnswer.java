package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Entity
@Table(name = "chat_bot_question_answer")
@NoArgsConstructor
public class ChatBotQuestionAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_bot_question_answer_id")
    private Long id;

    @Column(name = "question", columnDefinition = "nvarchar(500)")
    private String question;

    @Column(name = "answer", columnDefinition = "nvarchar(5000)")
    private String answer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    private Date createdAt;
}
