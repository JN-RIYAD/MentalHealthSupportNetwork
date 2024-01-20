package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "video")
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private Long id;

    @Column(name = "video_topic", columnDefinition = "nvarchar(250)")
    private String videoTopic;

    @Column(name = "producerName", columnDefinition = "nvarchar(100)")
    private String producerName;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "video_file_name", columnDefinition = "nvarchar(255)")
    private String videoFileName;

    @Column(name = "video_file_type", columnDefinition = "nvarchar(255)")
    private String videoFileType;

    @Temporal(TemporalType.DATE)
    @Column(name = "uploaded_date")
    private Date uploadedDate;
}
