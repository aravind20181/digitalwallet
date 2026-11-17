
package com.digitalwallet.digitalwallet.controller;

import com.digitalwallet.digitalwallet.dto.TransactionResponse;
import com.digitalwallet.digitalwallet.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/history")
    public ResponseEntity<?> getTransactionHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Page number cannot be negative");
        }

        if (size < 1 || size > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Page size must be between 1 and 100");
        }

        Page<TransactionResponse> transactions =
                transactionService.getTransactionHistory(page, size);

        return ResponseEntity.ok(transactions);
    }
}
