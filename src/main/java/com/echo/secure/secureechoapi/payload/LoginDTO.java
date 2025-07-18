package com.echo.secure.secureechoapi.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDTO {
    @NotEmpty
    private String userNameOrEmail;
    @NotEmpty
    private String password;
}
