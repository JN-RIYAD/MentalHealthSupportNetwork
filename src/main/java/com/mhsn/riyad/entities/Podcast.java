package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "podcast")
@NoArgsConstructor
public class Podcast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "podcast_id")
    private Long id;

    @Column(name = "podcast_topic", columnDefinition = "nvarchar(250)")
    private String podcastTopic;

    @Column(name = "producerName", columnDefinition = "nvarchar(100)")
    private String producerName;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "podcast_file_name", columnDefinition = "nvarchar(255)")
    private String podcastFileName;

    @Column(name = "podcast_file_type", columnDefinition = "nvarchar(255)")
    private String podcastFileType;

    @Temporal(TemporalType.DATE)
    @Column(name = "uploaded_date")
    private Date uploadedDate;

}