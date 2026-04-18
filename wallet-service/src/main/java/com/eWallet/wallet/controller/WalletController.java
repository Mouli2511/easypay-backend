package com.eWallet.wallet.controller;

import com.eWallet.wallet.dto.TopUpRequest;
import com.eWallet.wallet.dto.TransferRequest;
import com.eWallet.wallet.dto.WalletResponse;
import com.eWallet.wallet.entity.Transaction;
import com.eWallet.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WalletController {

    private final WalletService walletService;

    private String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("Unauthorized!");
        }
        return auth.getName();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Wallet Service is UP!");
    }

    @PostMapping("/create")
    public ResponseEntity<WalletResponse> createWallet() {
        return ResponseEntity.ok(walletService.createWallet(getCurrentUserEmail()));
    }

    @GetMapping("/balance")
    public ResponseEntity<WalletResponse> getBalance() {
        return ResponseEntity.ok(walletService.getWallet(getCurrentUserEmail()));
    }

    @PostMapping("/topup")
    public ResponseEntity<WalletResponse> topUp(@RequestBody TopUpRequest request) {
        return ResponseEntity.ok(walletService.topUp(getCurrentUserEmail(), request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<WalletResponse> transfer(@RequestBody TransferRequest request) {
        return ResponseEntity.ok(walletService.transfer(getCurrentUserEmail(), request));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions() {
        return ResponseEntity.ok(walletService.getTransactions(getCurrentUserEmail()));
    }
}