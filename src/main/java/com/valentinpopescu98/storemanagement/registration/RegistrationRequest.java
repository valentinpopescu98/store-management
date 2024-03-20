package com.valentinpopescu98.storemanagement.registration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class RegistrationRequest {

    @NotNull(message = "User must have a username")
    @NotEmpty(message = "Username must not be blank")
    private final String username;
    @NotNull(message = "User must have a password")
    @NotEmpty(message = "Password must not be blank")
    private final String password;
    @NotNull(message = "User must have an authority")
    @NotEmpty(message = "Authority must not be blank")
    private final String authority;

}
