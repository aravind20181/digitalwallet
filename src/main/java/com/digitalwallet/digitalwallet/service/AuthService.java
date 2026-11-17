
package com.digitalwallet.digitalwallet.service;

import com.digitalwallet.digitalwallet.model.User;
import com.digitalwallet.digitalwallet.dto.LoginRequest;
import com.digitalwallet.digitalwallet.dto.LoginResponse;
import com.digitalwallet.digitalwallet.repository.UserRepository;
import com.digitalwallet.digitalwallet.exception.InvalidCredentialsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        // Verify password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            throw new InvalidCredentialsException();
        }

        // Check whether the account is enabled
        if (!user.isEnabled()) {
            throw new InvalidCredentialsException();
        }

        // Generate JWT token
        String token = jwtService.generateToken(user);

        // Return safe login response
        return new LoginResponse(
                token,
                "Bearer",
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }
}

