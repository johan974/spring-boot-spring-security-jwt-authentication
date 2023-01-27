package com.bezkoder.springjwt.webclient2controller;

import com.bezkoder.springjwt.security.jwt.AuthEntryPointJwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Random;

@RestController
public class WebclientAccessToController {
  private static final Logger logger = LoggerFactory.getLogger(WebclientAccessToController.class);

  private WebClient webClientSignup;
  private WebClient webClientSecured;

  private String securedUrl = "http://example.com/api/auth";

  public WebclientAccessToController(@Qualifier("webclient-signup")WebClient webClientSignup,
                                     @Value("${secured.url}") String securedUrl) {
    this.webClientSignup = webClientSignup;
    this.securedUrl = securedUrl;
  }

  @GetMapping("/webclient")
  public String accessSecuredResourceViaSignupSigninTokenAccess() {

    // Step 1: signup
    SignupRequest signupRequest = new SignupRequest( "johan" + System.currentTimeMillis(),
            "jj974" + System.currentTimeMillis() + "@h.com", "mypassword",
            new String[] { "admin"});
    SignupResponse signupResponse = webClientSignup.post()
            .uri(uriBuilder -> uriBuilder.path("/signup").build())
            .body(BodyInserters.fromValue(signupRequest))
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError,response -> {
              logger.error("4xx error");
              return Mono.error(new RuntimeException("4xx"));
            })
            .onStatus(HttpStatus::is5xxServerError, response -> {
              logger.error("5xx error");
              return Mono.error(new RuntimeException("5xx"));
            })
            .bodyToMono(SignupResponse.class)
//            .retryWhen(Retry.max(5))
//            .timeout(Duration.ofSeconds(2),
//                    Mono.just(new SignupResponse( "Timedout!")))
            .block();
    logger.info( "Signup response: " + signupResponse);

    // Step 1b: better error handling
    // Step 1c: split in parts (second being response block).

    // Step 2: signin -> get token
    SigninRequest signinRequest = new SigninRequest( signupRequest.getUsername(), signupRequest.getPassword());
    SigninResponse signinResponse = webClientSignup.post()
            .uri(uriBuilder -> uriBuilder.path("/signin").build())
            .body(BodyInserters.fromValue(signinRequest))
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError,response -> {
              logger.error("4xx error");
              return Mono.error(new RuntimeException("4xx"));
            })
            .onStatus(HttpStatus::is5xxServerError, response -> {
              logger.error("5xx error");
              return Mono.error(new RuntimeException("5xx"));
            })
            .bodyToMono(SigninResponse.class)
            .retryWhen(Retry.max(5))
            .timeout(Duration.ofSeconds(2),
                    Mono.just(new SigninResponse( 0, "Timedout!", null, new String[0], null, null)))
            .block();
    logger.info( "Signin response: " + signinResponse);
    logger.info( "Signin response.accessTooken: " + signinResponse.getAccessToken());

    // Step 3: use token to access secured resource
    WebClient securedWebclient = WebClient.builder().baseUrl(securedUrl)
            .defaultHeader("Authorization", "Bearer " + signinResponse.getAccessToken())
            .defaultHeader("Content-Type", "application/xml")
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().wiretap(true)
            ))
            .build();
    String result = securedWebclient.get()
            .uri(uriBuilder -> uriBuilder.path("/admin").build())
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError,response -> {
              logger.error("4xx error");
              return Mono.error(new RuntimeException("4xx"));
            })
            .onStatus(HttpStatus::is5xxServerError, response -> {
              logger.error("5xx error");
              return Mono.error(new RuntimeException("5xx"));
            })
            .bodyToMono(String.class)
            .block();

    return result;
  }


}

/* Basepath and URI
===================
final WebClient webClient = WebClient
.builder()
.baseUrl("http://localhost")
.build();
webClient
.get()
.uri(uriBuilder -> uriBuilder.path("api/v2/json/test").build())
.exchange();
 */

/* Post with Object:
   .body(BodyInserters.fromValue(crawlRequest))
 */
