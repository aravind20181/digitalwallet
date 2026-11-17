
package com.digitalwallet.digitalwallet.dto;

import java.time.Instant;

public class UserResponse {

    private Long id;
    private String fullName;
    private String mobileNumber;
    private String email;
    private Instant createdAt;

    public UserResponse() {
    }

    public UserResponse(Long id, String fullName,
                        String mobileNumber, String email,
                        Instant createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}