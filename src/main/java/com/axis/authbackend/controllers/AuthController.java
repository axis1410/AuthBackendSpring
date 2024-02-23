package com.axis.authbackend.controllers;

import com.axis.authbackend.model.User;
import com.axis.authbackend.services.AuthService;
import com.axis.authbackend.utils.JwtUtility;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> postLogin(@RequestBody User user) {
        String accessToken = JwtUtility.generateAccessToken(user.getName(), user.getEmail());

        User savedUser = authService.login(user);

        Map<String, Object> response = new HashMap<>();

        response.put("access_token", accessToken);
        response.put("refresh_token", savedUser.getRefreshToken());
        response.put("user", savedUser);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> postLogout(@RequestBody User user) {
        authService.logout(user);

        return ResponseEntity.ok().body("Logged out");
    }

}
