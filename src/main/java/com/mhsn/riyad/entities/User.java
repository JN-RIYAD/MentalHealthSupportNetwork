package com.mhsn.riyad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "role")
    private String role;

    @Column(name = "user_name", columnDefinition = "nvarchar(100)")
    private String userName;

    @Column(name = "email", length = 100)
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

    @Column(name = "confirm_password", columnDefinition = "nvarchar(500)")
    private String confirmPassword;

//    @Column(name = "certificate_url")
//    private String certificateUrl;




}
