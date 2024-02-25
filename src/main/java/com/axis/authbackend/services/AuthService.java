package com.axis.authbackend.services;

import com.axis.authbackend.model.User;
import com.axis.authbackend.repository.AuthRepository;
import com.axis.authbackend.utils.JwtUtility;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;

    public User login(User user) {
        User existingUser = doesUserExist(user.getEmail());

        if(existingUser == null) {
            authRepository.save(user);
            User savedUser = authRepository.findUserByEmail(user.getEmail());

            if(savedUser.getRefreshToken() == null) {
                String refreshToken = JwtUtility.generateRefreshToken(savedUser.getId());
                savedUser.setRefreshToken(refreshToken);
            }

            Long id = savedUser.getId();
            return authRepository.save(savedUser);
        }

        String updatedRefreshToken = JwtUtility.generateRefreshToken(existingUser.getId());
        existingUser.setRefreshToken(updatedRefreshToken);

        return authRepository.save(existingUser);
    }

    public User logout(String accessToken) {
//        System.out.println("in logout method");
//        User existingUser = doesUserExist(user.getEmail());
//        existingUser.setRefreshToken(null);

        Claims claims = JwtUtility.extractAllClaims(accessToken);
        User existingUser = authRepository.findUserByEmail(claims.getSubject());

        existingUser.setRefreshToken(null);

        return authRepository.save(existingUser);
    }


    public boolean isTokenValid(String accessToken) {
        boolean isAccessTokenExpired = JwtUtility.isTokenExpired(accessToken);

        if(isAccessTokenExpired) {
            return false;
        }

        Claims claims = JwtUtility.extractAllClaims(accessToken);

        User user = authRepository.findUserByEmail(claims.getSubject());

        return user != null;
    }

    public String generateNewAccessTokenFromOldAccessToken(String accessToken) {
        Claims claims = JwtUtility.extractAllClaims(accessToken);
        String email = claims.getSubject();

        return JwtUtility.generateAccessToken(email);
    }

    public User getUserFromRefreshToken(String refreshToken) {
        System.out.println("in get user from refresh token");
        return authRepository.findUserByRefreshToken(refreshToken);
    }

    public User doesUserExist(String email) {
        return authRepository.findUserByEmail(email);

    }

}
