
package com.digitalwallet.digitalwallet.service;

import com.digitalwallet.digitalwallet.dto.TransactionResponse;
import com.digitalwallet.digitalwallet.model.User;
import com.digitalwallet.digitalwallet.model.Transaction;
import com.digitalwallet.digitalwallet.repository.UserRepository;
import com.digitalwallet.digitalwallet.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Get logged-in user's email from JWT
    public String getLoggedInUserEmail() {

        return org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    // Get transaction history of logged-in user
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactionHistory(
            int page,
            int size) {

        String email = getLoggedInUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Transaction> transactions =
                transactionRepository.findBySenderIdOrRecipientId(
                        user.getId(),
                        user.getId(),
                        pageable
                );

        return transactions.map(transaction ->
                new TransactionResponse(
                        transaction.getId(),
                        transaction.getSender().getFullName(),
                        transaction.getSender().getEmail(),
                        transaction.getRecipient().getFullName(),
                        transaction.getRecipient().getEmail(),
                        transaction.getAmount(),
                        transaction.getCurrency(),
                        transaction.getStatus(),
                        transaction.getCreatedAt()
                )
        );
    }
}

