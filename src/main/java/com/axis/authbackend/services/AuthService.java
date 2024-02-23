package com.axis.authbackend.services;

import com.axis.authbackend.model.User;
import com.axis.authbackend.repository.AuthRepository;
import com.axis.authbackend.utils.JwtUtility;
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

            Long id = savedUser.getId();



            String refreshToken = JwtUtility.generateRefreshToken(id);
            savedUser.setRefreshToken(refreshToken);

            return authRepository.save(savedUser);
        }

        String updatedRefreshToken = JwtUtility.generateRefreshToken(existingUser.getId());
        existingUser.setRefreshToken(updatedRefreshToken);

        return authRepository.save(existingUser);
    }

    public User logout(User user) {
        User existingUesr = doesUserExist(user.getEmail());
        existingUesr.setRefreshToken(null);
        return authRepository.save(existingUesr);
    }

    public User doesUserExist(String email) {
        return authRepository.findUserByEmail(email);

    }

}
