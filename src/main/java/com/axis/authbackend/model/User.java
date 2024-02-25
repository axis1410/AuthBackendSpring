package com.axis.authbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "refresh_token")
    private String refreshToken;

//    @Column(name = "child_name")
//    private String childName;
//
//    @Column(name = "child_age")
//    private int childAge;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, String refreshToken) {
        this.name = name;
        this.email = email;
        this.refreshToken = refreshToken;
    }

//    public User (String name, String email, String refreshToken, String childName, int childAge) {
//        this.name = name;
//        this.email = email;
//        this.refreshToken = refreshToken;
//        this.childName = childName;
//        this.childAge = childAge;
//    }
}
