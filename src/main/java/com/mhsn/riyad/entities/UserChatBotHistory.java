package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_chat_bot_history")
@NoArgsConstructor
@AllArgsConstructor
public class UserChatBotHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_chat_bot_history_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "question", columnDefinition = "nvarchar(500)")
    private String question;

    @ManyToOne
    @JoinColumn(name = "chat_bot_question_answer_id")
    private ChatBotQuestionAnswer chatBotQuestionAnswer;
}
