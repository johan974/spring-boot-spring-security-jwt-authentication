package com.bezkoder.springjwt.webclient2controller;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SignupRequest {
  private String username;
  private String email;
  private String password;
  private String[] role;
}
