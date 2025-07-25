package com.echo.secure.secureechoapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vault")
public class Vault {

    @Id()
    private String userName;

    @Lob
    private String encryptedData;

    private String key;
    private Integer failedAttempts;
    private Boolean isUpdated = false;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_name")
    private Customer customer;
}
