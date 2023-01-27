package com.bezkoder.springjwt.webclient2controller;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SigninRequest {
  private String username;
  private String password;
}
