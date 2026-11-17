package com.digitalwallet.digitalwallet.dto;

import java.math.BigDecimal;

public class WalletResponse {

    private Long id;
    private BigDecimal balance;
    private String currency;

    public WalletResponse(Long id, BigDecimal balance, String currency) {
        this.id = id;
        this.balance = balance;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }
}