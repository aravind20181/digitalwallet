
package com.digitalwallet.digitalwallet.controller;

import com.digitalwallet.digitalwallet.dto.WalletResponse;
import com.digitalwallet.digitalwallet.service.WalletService;
import com.digitalwallet.digitalwallet.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponse> getWallet(
            @PathVariable Long userId,
            Authentication authentication) {

        Long loggedInUserId = walletService.getUserIdByEmail(
                authentication.getName()
        );

        if (!loggedInUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }

        Wallet wallet = walletService.getWalletByUserId(userId);

        WalletResponse response = new WalletResponse(
                wallet.getId(),
                wallet.getBalance(),
                wallet.getCurrency()
        );

        return ResponseEntity.ok(response);
    }
}