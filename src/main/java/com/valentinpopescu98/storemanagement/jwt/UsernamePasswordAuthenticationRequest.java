package com.valentinpopescu98.storemanagement.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernamePasswordAuthenticationRequest {

    private String username;
    private String password;

}
