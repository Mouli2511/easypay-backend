package com.eWallet.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponse {
    private Long walletId;
    private String userEmail;
    private BigDecimal balance;
    private String status;
    private String message;
}