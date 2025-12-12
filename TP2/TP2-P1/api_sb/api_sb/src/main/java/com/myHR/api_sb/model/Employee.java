package com.myHR.api_sb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 250)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 250)
    private String lastName;

    @Column(nullable = false, length = 250)
    private String mail;

    @Column(nullable = false, length = 250)
    private String password;


    public void setId(Long id) {
        this.id = id;
    }
}
