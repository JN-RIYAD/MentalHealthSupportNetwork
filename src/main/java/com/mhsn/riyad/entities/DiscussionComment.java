package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "discussion_comment")
@NoArgsConstructor
public class DiscussionComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_content", columnDefinition = "nvarchar(500)", nullable = false)
    private String commentContent;

    @Temporal(TemporalType.DATE)
    @Column(name = "comment_date")
    private Date commentDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_date", nullable = true)
    private Date updatedDate;

    @ManyToOne
    @JoinColumn(name = "discussion_id", nullable = false)
    private Discussion discussion;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
