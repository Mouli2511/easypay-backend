package com.eWallet.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Long id;
    private String transactionId;
    private String payerEmail;
    private String merchantName;
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
    private String message;
}