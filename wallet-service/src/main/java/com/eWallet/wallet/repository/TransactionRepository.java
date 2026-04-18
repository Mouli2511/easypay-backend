package com.eWallet.wallet.repository;

import com.eWallet.wallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderEmailOrReceiverEmailOrderByCreatedAtDesc(
            String senderEmail, String receiverEmail);
}