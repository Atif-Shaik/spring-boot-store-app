package com.atifstudios.store.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotBlank(message = "Email is required!")
    @Email(message = "Email must be valid!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 6, max = 25, message = "Password must be between 6 to 25 characters long!")
    private String password;
}
