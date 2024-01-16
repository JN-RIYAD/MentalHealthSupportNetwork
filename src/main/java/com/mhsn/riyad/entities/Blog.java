package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Data
@Entity
@Table(name = "blog")
@NoArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", columnDefinition = "nvarchar(250)")
    private String title;

    @Column(name = "topic", columnDefinition = "nvarchar(100)")
    private String topic;

    @Column(name = "author", columnDefinition = "nvarchar(100)")
    private String author;

    @Column(name = "description", columnDefinition = "nvarchar(1000)")
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "published_date")
    private Date publishedDate;
}
