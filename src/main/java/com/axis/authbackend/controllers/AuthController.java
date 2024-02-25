package com.axis.authbackend.controllers;

import com.axis.authbackend.model.User;
import com.axis.authbackend.services.AuthService;
import com.axis.authbackend.utils.JwtUtility;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> postLogin(@RequestBody User user) {
        String accessToken = JwtUtility.generateAccessToken(user.getEmail());

        User savedUser = authService.login(user);

        Map<String, Object> response = new HashMap<>();

        response.put("access_token", accessToken);
        response.put("refresh_token", savedUser.getRefreshToken());
        response.put("user", savedUser);

        return ResponseEntity.ok().body(response);
    }

//    @GetMapping("/token")
//    public ResponseEntity<Object> postToken(@RequestHeader String Authorization) {
//        String accessToken = Authorization.substring(7);
//
//        boolean isValid = authService.isTokenValid(accessToken);
//
//        if (!isValid) {
//            return ResponseEntity.status(401).body("Invalid token");
//        }
//
//        Map<String, Object> response = new HashMap<>();
//
//        String newAccessToken = authService.generateNewAccessTokenFromOldAccessToken(accessToken);
//
//        response.put("access_token", newAccessToken);
//
//        System.out.println(response);
//        return ResponseEntity.ok().body(response);
//    }

    @GetMapping("/token")
    public ResponseEntity<Object> getToken(@RequestHeader("Authorization") String authorizationHeader, HttpServletResponse response) throws IOException {
        String accessToken = authorizationHeader.substring(7);

        try {
            JwtUtility.extractAllClaims(accessToken); // This method will throw an ExpiredJwtException if the token is expired
        } catch (ExpiredJwtException e) {
            String newAccessToken = authService.generateNewAccessTokenFromOldAccessToken(accessToken);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("access_token", newAccessToken);

            return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
        }

        return ResponseEntity.status(401).body("Invalid token");
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> postLogout(@RequestHeader String Authorization) {
        String accessToken = Authorization.substring(7);

        boolean isValid = authService.isTokenValid(accessToken);

        if (!isValid) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        authService.logout(accessToken);

        return ResponseEntity.ok().body("Logged out");
    }

    @GetMapping("/me")
    public ResponseEntity<Object> postMe(@RequestHeader String Authorization) {
        String accessToken = Authorization.substring(7);
        System.out.println(accessToken);

        boolean isValid = authService.isTokenValid(accessToken);

        if (!isValid) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> response = new HashMap<>();


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
