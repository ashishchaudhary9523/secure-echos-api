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
    private int failedAttempts = 0;

}
