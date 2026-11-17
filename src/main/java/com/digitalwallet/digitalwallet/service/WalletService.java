
package com.digitalwallet.digitalwallet.service;

import com.digitalwallet.digitalwallet.model.User;
import com.digitalwallet.digitalwallet.model.Wallet;
import com.digitalwallet.digitalwallet.repository.UserRepository;
import com.digitalwallet.digitalwallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUser_Id(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Wallet not found"));
    }

    @Transactional(readOnly = true)
    public Long getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        return user.getId();
    }
}
