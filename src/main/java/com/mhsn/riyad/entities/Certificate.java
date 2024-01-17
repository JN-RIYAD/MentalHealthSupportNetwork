package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "certificate")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = "user")
public class Certificate {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Long id;

    @Column(name = "certificate_url")
    private String certificateUrl;

    @Column(name = "file_type")
    private String certificateType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}