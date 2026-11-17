
package com.digitalwallet.digitalwallet.repository;
import com.digitalwallet.digitalwallet.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findBySenderIdOrRecipientId(
            Long senderId,
            Long recipientId,
            Pageable pageable
    );
}
