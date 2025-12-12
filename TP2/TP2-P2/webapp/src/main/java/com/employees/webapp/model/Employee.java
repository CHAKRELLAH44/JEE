package com.employees.webapp.model;

import lombok.Data;

@Data
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String mail;
    private String password;
}
//Représente les données des employés récupérées depuis l’API REST