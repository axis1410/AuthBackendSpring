package com.axis.authbackend.controllers;

import com.axis.authbackend.middleware.Middleware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public ResponseEntity<Object> hello(@RequestHeader String Authorization) {
        String accessToken = Authorization.substring(7);
//        System.out.println(accessToken);

//        boolean isTokenValid = Middleware.(accessToken);

//        System.out.println(isTokenValid);

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Hello");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
