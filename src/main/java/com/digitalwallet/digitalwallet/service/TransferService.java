
package com.digitalwallet.digitalwallet.service;

import com.digitalwallet.digitalwallet.model.User;
import com.digitalwallet.digitalwallet.model.Wallet;
import com.digitalwallet.digitalwallet.model.Transaction;

import com.digitalwallet.digitalwallet.repository.UserRepository;
import com.digitalwallet.digitalwallet.repository.WalletRepository;
import com.digitalwallet.digitalwallet.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransferService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Get logged-in user's email from JWT authentication
    public String getLoggedInUserEmail() {

        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    // Find recipient using email or mobile number
    public User findRecipient(String recipient) {

        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException(
                    "Recipient email or mobile number is required");
        }

        recipient = recipient.trim();

        if (recipient.contains("@")) {

            return userRepository.findByEmail(recipient)
                    .orElseThrow(() ->
                            new IllegalArgumentException("Recipient not found"));
        }

        return userRepository.findByMobileNumber(recipient)
                .orElseThrow(() ->
                        new IllegalArgumentException("Recipient not found"));
    }

    // Validate sender and recipient
    public void validateRecipient(User sender, User recipient) {

        if (!sender.isEnabled()) {
            throw new IllegalArgumentException(
                    "Sender account is disabled");
        }

        if (!recipient.isEnabled()) {
            throw new IllegalArgumentException(
                    "Recipient account is disabled");
        }

        if (sender.getId().equals(recipient.getId())) {
            throw new IllegalArgumentException(
                    "You cannot transfer money to yourself");
        }
    }

    // Validate transfer amount and sender balance
    public void validateBalance(Wallet senderWallet, BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Transfer amount must be greater than zero");
        }

        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient wallet balance");
        }
    }

    // Validate wallet currencies
    public void validateCurrency(
            Wallet senderWallet,
            Wallet recipientWallet) {

        if (senderWallet.getCurrency() == null ||
                recipientWallet.getCurrency() == null ||
                !senderWallet.getCurrency()
                        .equals(recipientWallet.getCurrency())) {

            throw new IllegalArgumentException(
                    "Currency mismatch or missing currency");
        }
    }

    // Find and lock wallet inside the transaction
    private Wallet getWalletForUpdate(Long userId) {

        return walletRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Wallet not found"));
    }

    // Execute complete money transfer
    @Transactional
    public void executeTransfer(
            String recipientIdentifier,
            BigDecimal amount) {

        // 1. Identify sender using JWT
        String email = getLoggedInUserEmail();

        User sender = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Sender not found"));

        // 2. Find recipient
        User recipient = findRecipient(recipientIdentifier);

        // 3. Validate sender and recipient
        validateRecipient(sender, recipient);

        Wallet senderWallet;
        Wallet recipientWallet;

        // 4. Lock both wallets in consistent user-ID order
        if (Long.compare(sender.getId(), recipient.getId()) < 0) {

            senderWallet = getWalletForUpdate(sender.getId());
            recipientWallet = getWalletForUpdate(recipient.getId());

        } else {

            recipientWallet = getWalletForUpdate(recipient.getId());
            senderWallet = getWalletForUpdate(sender.getId());
        }

        // 5. Validate balance and currency
        validateBalance(senderWallet, amount);

        validateCurrency(senderWallet, recipientWallet);

        // 6. Debit sender
        BigDecimal senderNewBalance =
                senderWallet.getBalance().subtract(amount);

        senderWallet.setBalance(senderNewBalance);

        // 7. Credit recipient
        BigDecimal recipientNewBalance =
                recipientWallet.getBalance().add(amount);

        recipientWallet.setBalance(recipientNewBalance);

        // 8. Save both wallets
        walletRepository.save(senderWallet);
        walletRepository.save(recipientWallet);

        // 9. Create transaction history record
        Transaction transaction = new Transaction();

        transaction.setSender(sender);
        transaction.setRecipient(recipient);
        transaction.setAmount(amount);
        transaction.setCurrency(senderWallet.getCurrency());
        transaction.setStatus("SUCCESS");
        transaction.setCreatedAt(LocalDateTime.now());

        // 10. Save transaction in database
        transactionRepository.save(transaction);
    }
}
