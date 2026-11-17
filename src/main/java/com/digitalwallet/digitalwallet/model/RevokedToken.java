package com.digitalwallet.digitalwallet.model;

import jakarta.persistence.*;

import java.time.Instant;
@Entity
@Table(name="revoked_tokens")
public class RevokedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 1000)
    private String token;

    @Column(nullable = false)
    private Instant expiresAt;

    public RevokedToken(){
    }

    public RevokedToken(String token,Instant expiresAt){
        this.token=token;
        this.expiresAt=expiresAt;
    }

    public Long getId(){
        return id;
    }

    public String getToken(){
        return token;
    }

    public Instant getExpiresAt(){
        return expiresAt;
    }
}
