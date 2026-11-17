
package com.digitalwallet.digitalwallet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private String senderName;
    private String senderEmail;
    private String recipientName;
    private String recipientEmail;
    private BigDecimal amount;
    private String currency;
    private String status;
    private LocalDateTime createdAt;

    public TransactionResponse(
            Long id,
            String senderName,
            String senderEmail,
            String recipientName,
            String recipientEmail,
            BigDecimal amount,
            String currency,
            String status,
            LocalDateTime createdAt) {

        this.id = id;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.recipientName = recipientName;
        this.recipientEmail = recipientEmail;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }

    public String getSenderName() { return senderName; }

    public String getSenderEmail() { return senderEmail; }

    public String getRecipientName() { return recipientName; }

    public String getRecipientEmail() { return recipientEmail; }

    public BigDecimal getAmount() { return amount; }

    public String getCurrency() { return currency; }

    public String getStatus() { return status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}