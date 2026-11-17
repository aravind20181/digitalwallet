package com.digitalwallet.digitalwallet.service;

import com.digitalwallet.digitalwallet.model.RevokedToken;
import com.digitalwallet.digitalwallet.repository.RevokedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RevokedTokenService {

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    public void revokeToken(String token, Instant expiresAt){
        if(!revokedTokenRepository.existsByToken(token)){
            RevokedToken revokedToken =
                    new RevokedToken(token,expiresAt);

            revokedTokenRepository.save(revokedToken);
        }
    }

    public boolean isTokenRevoked(String token){
        return revokedTokenRepository.existsByToken(token);
    }
}
