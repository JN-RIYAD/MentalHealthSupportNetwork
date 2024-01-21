package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "therapist")
@NoArgsConstructor
public class Therapist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "therapist_id")
    private Long id;

    @Column(name = "role", length = 10)
    private String role;

    @Column(name = "therapist_name", columnDefinition = "nvarchar(100)")
    private String therapistName;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "age")
    private Integer age;

    @Column(name = "mobile_no", length = 50)
    private String mobileNo;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address", columnDefinition = "nvarchar(500)")
    private String address;

    @Column(name = "password", columnDefinition = "nvarchar(500)")
    private String password;

    public void setAddedDate(Date date) {
    }

    public void setUpdatedDate(Date date) {
    }
}
