package com.digitalwallet.digitalwallet.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class TransferRequest {

    @NotBlank(message = "Recipient is required")
    private String recipient;

    @NotNull(message="Transfer amount is required")
    @DecimalMin(value="1",message="Minimum transfer amount is 1")
    private BigDecimal amount;


    public String getRecipient(){
        return recipient;
    }

    public void setRecipient(String recipient){
        this.recipient=recipient;
    }

    public BigDecimal getAmount(){
        return amount;
    }

    public void setAmount(BigDecimal amount){
        this.amount=amount;
    }

}