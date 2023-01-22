package com.bezkoder.springjwt.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test2")
public class TestAlternativeController {

  @GetMapping("/user")
  public String userAccess() {
    return "User 2 Content.";
  }

  @GetMapping("/mod")
  public String moderatorAccess() {
    return "Moderator 2 Board.";
  }

  @GetMapping("/admin")
  public String adminAccess() {
    return "Admin 2 Board.";
  }

}
