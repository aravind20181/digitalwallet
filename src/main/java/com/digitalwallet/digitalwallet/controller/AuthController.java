
package com.digitalwallet.digitalwallet.controller;

import com.digitalwallet.digitalwallet.dto.LoginRequest;
import com.digitalwallet.digitalwallet.dto.LoginResponse;
import com.digitalwallet.digitalwallet.dto.ForgotPasswordRequest;
import com.digitalwallet.digitalwallet.dto.ResetPasswordRequest;
import com.digitalwallet.digitalwallet.service.AuthService;
import com.digitalwallet.digitalwallet.service.JwtService;
import com.digitalwallet.digitalwallet.service.RevokedTokenService;
import com.digitalwallet.digitalwallet.service.PasswordResetService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RevokedTokenService revokedTokenService;

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        Instant expiresAt = jwtService.extractExpiration(token);

        revokedTokenService.revokeToken(token, expiresAt);

        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        passwordResetService.createResetToken(request.getEmail());

        return ResponseEntity.ok(
                "If the account exists, password reset instructions will be sent."
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        passwordResetService.resetPassword(
                request.getToken(),
                request.getNewPassword()
        );

        return ResponseEntity.ok("Password reset successfully");
    }
}

