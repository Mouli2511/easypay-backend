package com.eWallet.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderEmail;
    private String receiverEmail;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.SUCCESS;

    private String description;
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum TransactionType {
        TOPUP, TRANSFER, PAYMENT, REFUND
    }

    public enum TransactionStatus {
        SUCCESS, FAILED, PENDING
    }
}