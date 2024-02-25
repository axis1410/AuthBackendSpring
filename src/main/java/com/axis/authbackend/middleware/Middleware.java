package com.axis.authbackend.middleware;

import com.axis.authbackend.model.User;
import com.axis.authbackend.repository.AuthRepository;
import com.axis.authbackend.services.AuthService;
import com.axis.authbackend.utils.JwtUtility;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class Middleware extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURL().toString().contains("/login") || request.getRequestURL().toString().contains("/token")) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = request.getHeader("Authorization").substring(7);

        try {
            JwtUtility.extractAllClaims(accessToken); // This method will throw an ExpiredJwtException if the token is expired
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = JwtUtility.extractEmailFromAccessToken(accessToken);

        if (email == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User user = authService.doesUserExist(email);

        boolean isRefreshTokenExpired = JwtUtility.isTokenExpired(user.getRefreshToken());

        if (isRefreshTokenExpired) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
