package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "discussion")
@NoArgsConstructor
public class Discussion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discussion_id")
    private Long id;

    @Column(name = "query_topic", columnDefinition = "nvarchar(250)")
    private String queryTopic;

    @Column(name = "inquirer_name", columnDefinition = "nvarchar(100)")
    private String inquirerName;

    @Column(name = "inquirer_gender", length = 10)
    private String inquirerGender;

    @Column(name = "query_description", columnDefinition = "nvarchar(3000)")
    private String queryDescription;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "query_date")
    private Date queryDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated_date")
    private Date updatedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "discussion")
    private List<DiscussionComment> commentList;

    @Override
    public String toString() {
        return "Discussion{" +
                "id=" + id +
                ", queryTopic='" + queryTopic + '\'' +
                ", inquirerName='" + inquirerName + '\'' +
                ", inquirerGender='" + inquirerGender + '\'' +
                ", queryDescription='" + queryDescription + '\'' +
                ", queryDate=" + queryDate +
                ", updatedDate=" + updatedDate +
                ", user=" + user +
                ", commentList=" + commentList +
                '}';
    }


}
