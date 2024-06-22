package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    @Column(name = "number_of_participants")
    private Integer numberOfParticipants;

    @Column(name = "event_status")
    private String eventStatus;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    private List<Participant> participantList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    private List<EventComment> commentList;

    // Separate date and time getters
    public String getEventDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return dateAndTime.toLocalDate().format(formatter);
    }

    public String getEventTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return dateAndTime.format(formatter);
    }

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
                ", numberOfParticipants='" + numberOfParticipants + '\'' +
                ", eventStatus='" + eventStatus + '\'' +
                ", participantList=" + participantList +
                ", commentList=" + commentList +
                '}';
    }


}
