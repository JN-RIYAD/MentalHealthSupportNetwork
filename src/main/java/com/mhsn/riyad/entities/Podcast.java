package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "podcast")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = "user")
public class Podcast {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "podcast_id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "topic",  columnDefinition = "nvarchar(100)")
    private String topic;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "producer", columnDefinition = "nvarchar(100)")
    private String producer;

    @Column(name = "duration")
    private Integer duration;

    @Temporal(TemporalType.DATE)
    @Column(name = "published_date")
    private Date publishedDate;

}