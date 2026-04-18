package com.eWallet.payment.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private String merchantName;
    private String merchantId;
    private BigDecimal amount;
    private String paymentMethod;
    private String description;
}