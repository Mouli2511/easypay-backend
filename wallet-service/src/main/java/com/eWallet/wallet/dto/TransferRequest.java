package com.eWallet.wallet.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    private String receiverEmail;
    private BigDecimal amount;
    private String description;
}