package com.mhsn.riyad.entities;

import com.mhsn.riyad.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_name", columnDefinition = "nvarchar(100)")
    private String userName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "age")
    private Integer age;

    @Column(name = "mobile_no", length = 50)
    private String mobileNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "address", columnDefinition = "nvarchar(500)")
    private String address;

    @Column(name = "password", columnDefinition = "nvarchar(500)")
    private String password;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Certificate> certificateList;




}
