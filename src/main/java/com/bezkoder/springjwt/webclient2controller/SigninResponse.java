package com.bezkoder.springjwt.webclient2controller;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SigninResponse {
  private int id;
  private String username;
  private String email;
  private String[] roles;
  private String tokenType;
  private String accessToken;
}

/*
{
    "id": 2,
    "username": "mod",
    "email": "mod@bezoeker.com",
    "roles": [
        "ROLE_MODERATOR"
    ],
    "tokenType": "Bearer",
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtb2QiLCJpYXQiOjE2NzQ4MjcwNjQsImV4cCI6MTY3NDkxMzQ2NH0.0sMVf_7rVIofL4AlDixNPG3PVCuOYcKIMeihIM0JaC39tZbaxM8ly3MGN1iLTF5HTSiC7G_vJTSM8Xc-htQp7Q"
}
 */
