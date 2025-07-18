package com.echo.secure.secureechoapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "customer" , uniqueConstraints = {@UniqueConstraint(columnNames = {"userName"}) ,
        @UniqueConstraint(columnNames = {"email"})})
public class Customer {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    private String name;
    @Id
    private String userName;
    @Column(unique = true , nullable = false)
    private String email;
    private String password;

}