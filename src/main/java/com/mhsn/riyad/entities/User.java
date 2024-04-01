package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "role", length = 10)
    private String role;

    @Column(name = "user_name", columnDefinition = "nvarchar(100)")
    private String userName;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "father_name", columnDefinition = "nvarchar(100)")
    private String fatherName;

    @Column(name = "mother_name", columnDefinition = "nvarchar(100)")
    private String motherName;

    @Column(name = "date_of_birth", columnDefinition = "nvarchar(100)")
    private String dateOfBirth;

    @Column(name = "age")
    private Integer age;

    @Column(name = "mobile_no", length = 50)
    private String mobileNo;

    @Column(name = "nid_no", length = 50)
    private String nidNo;

    @Column(name = "gender")
    private String gender;

    @Column(name = "religion")
    private String religion;

    @Column(name = "address", columnDefinition = "nvarchar(500)")
    private String address;

    @Column(name = "question1", columnDefinition = "nvarchar(100)")
    private String question1;

    @Column(name = "answer1", columnDefinition = "nvarchar(100)")
    private String answer1;

    @Column(name = "question2", columnDefinition = "nvarchar(100)")
    private String question2;

    @Column(name = "answer2", columnDefinition = "nvarchar(100)")
    private String answer2;

    @Column(name = "password", columnDefinition = "nvarchar(500)")
    private String password;

    @Column(name = "image_url", columnDefinition = "nvarchar(500)")
    private String imageUrl;

    @Temporal(TemporalType.DATE)
    @Column(name = "registration_date")
    private Date registrationDate;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", motherName='" + motherName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", age=" + age +
                ", mobileNo='" + mobileNo + '\'' +
                ", nidNo='" + nidNo + '\'' +
                ", gender='" + gender + '\'' +
                ", religion='" + religion + '\'' +
                ", address='" + address + '\'' +
                ", question1='" + question1 + '\'' +
                ", answer1='" + answer1 + '\'' +
                ", question1='" + question1 + '\'' +
                ", answer2='" + answer2 + '\'' +
                ", password='" + password + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}





