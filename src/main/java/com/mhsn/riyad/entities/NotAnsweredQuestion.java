package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "not_answered_question")
@NoArgsConstructor
public class NotAnsweredQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "not_answered_question_id")
    private Long id;

    @Column(name = "question", columnDefinition = "nvarchar(500)")
    private String question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
