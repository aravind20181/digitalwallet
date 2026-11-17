package com.digitalwallet.digitalwallet.dto;

public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private Long userId;
    private String fullName;
    private String email;

    public LoginResponse(
            String accessToken,
            String tokenType,
            Long userId,
            String fullName,
            String email) {

        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
}
