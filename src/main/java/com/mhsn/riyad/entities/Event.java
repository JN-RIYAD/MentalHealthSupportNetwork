package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "event")
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "title", columnDefinition = "nvarchar(100)")
    private String title;

    @Column(name = "description", columnDefinition = "nvarchar(1000)")
    private String description;

    @Column(name = "location", columnDefinition = "nvarchar(250)")
    private String location;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_and_time")
    private LocalDateTime dateAndTime;

    @Column(name = "organizer", columnDefinition = "nvarchar(300)")
    private String organizer;

    @Column(name = "speaker", columnDefinition = "nvarchar(100)")
    private String speaker;

    @Column(name = "speaker_designation", columnDefinition = "nvarchar(100)")
    private String speakerDesignation;

    @Column(name = "chief_guest", columnDefinition = "nvarchar(100)")
    private String chiefGuest;

    @Column(name = "chief_guest_designation", columnDefinition = "nvarchar(100)")
    private String chiefGuestDesignation;

    @Column(name = "banner_url", columnDefinition = "nvarchar(500)")
    private String bannerUrl;

    @Column(name = "banner_file_name", columnDefinition = "nvarchar(255)")
    private String bannerFileName;

    @Column(name = "banner_file_type", columnDefinition = "nvarchar(255)")
    private String bannerFileType;

    @Temporal(TemporalType.DATE)
    @Column(name = "uploaded_date")
    private Date uploadedDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    private List<Participant> participantList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    private List<EventComment> commentList;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", dateAndTime=" + dateAndTime +
                ", organizer='" + organizer + '\'' +
                ", speaker='" + speaker + '\'' +
                ", speakerDesignation='" + speakerDesignation + '\'' +
                ", chiefGuest='" + chiefGuest + '\'' +
                ", chiefGuestDesignation='" + chiefGuestDesignation + '\'' +
                ", bannerFileName='" + bannerFileName + '\'' +
                ", bannerFileType='" + bannerFileType + '\'' +
                ", uploadedDate=" + uploadedDate +
                ", participantList=" + participantList +
                ", commentList=" + commentList +
                '}';
    }


}
