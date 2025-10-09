package com.app.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name="app_user")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
}
