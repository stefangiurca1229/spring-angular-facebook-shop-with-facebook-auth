package com.springangularfacebook.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String jwt;

    public JwtResponse() {
    }

    public JwtResponse(String token) {
        jwt = token;
    }
}
