
package com.digitalwallet.digitalwallet.service;

import com.digitalwallet.digitalwallet.model.PasswordResetToken;
import com.digitalwallet.digitalwallet.model.User;
import com.digitalwallet.digitalwallet.repository.PasswordResetTokenRepository;
import com.digitalwallet.digitalwallet.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    public PasswordResetService(
            UserRepository userRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createResetToken(String email) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || !user.isEnabled()) {
            return;
        }

        List<PasswordResetToken> existingTokens =
                passwordResetTokenRepository
                        .findAllByUser_IdAndUsedFalse(user.getId());

        for (PasswordResetToken existingToken : existingTokens) {
            existingToken.setUsed(true);
        }

        passwordResetTokenRepository.saveAll(existingTokens);

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        PasswordResetToken resetToken = new PasswordResetToken();

        resetToken.setUser(user);
        resetToken.setTokenHash(hashToken(rawToken));
        resetToken.setExpiresAt(Instant.now().plusSeconds(900));
        resetToken.setUsed(false);

        passwordResetTokenRepository.save(resetToken);

        // The raw token must be delivered securely through an email service.
        // Do not return it in the API response.
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {

        String tokenHash = hashToken(rawToken);

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByTokenHashForUpdate(tokenHash)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid or expired reset token"));

        if (resetToken.isUsed()
                || !resetToken.getExpiresAt().isAfter(Instant.now())) {

            throw new IllegalArgumentException(
                    "Invalid or expired reset token");
        }

        User user = resetToken.getUser();

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(Instant.now());
        user.setTokenVersion(user.getTokenVersion() + 1);

        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    private String hashToken(String rawToken) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashedBytes =
                    digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hashedBytes);

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "SHA-256 algorithm unavailable", e);
        }
    }
}

