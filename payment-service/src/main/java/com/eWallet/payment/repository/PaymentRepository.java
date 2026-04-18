package com.eWallet.payment.repository;

import com.eWallet.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPayerEmailOrderByCreatedAtDesc(String payerEmail);
    Optional<Payment> findByTransactionId(String transactionId);
}