package com.digitalwallet.digitalwallet.controller;

import com.digitalwallet.digitalwallet.service.TransferService;
import com.digitalwallet.digitalwallet.dto.TransferRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/send")
    public String sendMoney(
            @Valid @RequestBody TransferRequest request) {

        transferService.executeTransfer(
                request.getRecipient(),
                request.getAmount()
        );

        return "Money transferred successfully";
    }

}