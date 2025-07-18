package com.echo.secure.secureechoapi.payload;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AccessDTO {
    @NotEmpty
    private String userName;
    @NotEmpty
    private String key;
}
