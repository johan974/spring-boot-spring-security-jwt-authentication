package com.bezkoder.springjwt.webclient2controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebclientConfig {
  @Bean("webclient-signup")
  public WebClient signingClient(@Value("${signup.url}") String signupUrl) {
    return WebClient.builder()
            .baseUrl(signupUrl)
            .build();
  }

//  @Bean("webclient-secured")
//  public WebClient securedClient(@Value("${secured.url}") String securedUrl, String securedToken) {
//    return WebClient.builder().baseUrl(securedUrl)
//            .defaultHeader("Authorization", "Bearer " + securedToken)
//            .defaultHeader("Content-Type", "application/xml")
//            .clientConnector(new ReactorClientHttpConnector(
//                    HttpClient.create().wiretap(true)
//            ))
//            .build();
//  }

}
