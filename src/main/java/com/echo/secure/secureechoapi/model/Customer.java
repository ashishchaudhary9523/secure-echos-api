package com.echo.secure.secureechoapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "customer" , uniqueConstraints = {@UniqueConstraint(columnNames = {"userName"}) ,
        @UniqueConstraint(columnNames = {"email"})})
public class Customer {

    private String name;
    @Id
    private String userName;
    @Column(unique = true , nullable = false)
    private String email;
    private String password;

    @OneToOne(mappedBy = "customer" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Vault vault;

}