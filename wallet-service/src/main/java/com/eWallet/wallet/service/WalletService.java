package com.eWallet.wallet.service;

import com.eWallet.wallet.dto.TopUpRequest;
import com.eWallet.wallet.dto.TransferRequest;
import com.eWallet.wallet.dto.WalletResponse;
import com.eWallet.wallet.entity.Transaction;
import com.eWallet.wallet.entity.Wallet;
import com.eWallet.wallet.repository.TransactionRepository;
import com.eWallet.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletResponse createWallet(String userEmail) {
        if (walletRepository.existsByUserEmail(userEmail)) {
            throw new RuntimeException("Wallet already exists for: " + userEmail);
        }
        Wallet wallet = new Wallet();
        wallet.setUserEmail(userEmail);
        wallet.setBalance(BigDecimal.ZERO);
        walletRepository.save(wallet);
        return toResponse(wallet, "Wallet created successfully!");
    }

    public WalletResponse getWallet(String userEmail) {
        Wallet wallet = walletRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Wallet not found!"));
        return toResponse(wallet, "Success");
    }

    @Transactional
    public WalletResponse topUp(String userEmail, TopUpRequest request) {
        Wallet wallet = walletRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Wallet not found!"));

        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        Transaction txn = new Transaction();
        txn.setReceiverEmail(userEmail);
        txn.setAmount(request.getAmount());
        txn.setType(Transaction.TransactionType.TOPUP);
        txn.setDescription(request.getDescription() != null ?
                request.getDescription() : "Wallet top-up");
        transactionRepository.save(txn);

        return toResponse(wallet, "Top-up successful!");
    }

    @Transactional
    public WalletResponse transfer(String senderEmail, TransferRequest request) {
        if (senderEmail.equals(request.getReceiverEmail())) {
            throw new RuntimeException("Cannot transfer to yourself!");
        }

        Wallet sender = walletRepository.findByUserEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender wallet not found!"));

        Wallet receiver = walletRepository.findByUserEmail(request.getReceiverEmail())
                .orElseThrow(() -> new RuntimeException("Receiver wallet not found!"));

        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance!");
        }

        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        sender.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(sender);

        receiver.setBalance(receiver.getBalance().add(request.getAmount()));
        receiver.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(receiver);

        Transaction txn = new Transaction();
        txn.setSenderEmail(senderEmail);
        txn.setReceiverEmail(request.getReceiverEmail());
        txn.setAmount(request.getAmount());
        txn.setType(Transaction.TransactionType.TRANSFER);
        txn.setDescription(request.getDescription() != null ?
                request.getDescription() : "Transfer");
        transactionRepository.save(txn);

        return toResponse(sender, "Transfer successful!");
    }

    public List<Transaction> getTransactions(String userEmail) {
        return transactionRepository
                .findBySenderEmailOrReceiverEmailOrderByCreatedAtDesc(
                        userEmail, userEmail);
    }

    private WalletResponse toResponse(Wallet wallet, String message) {
        return new WalletResponse(
                wallet.getId(),
                wallet.getUserEmail(),
                wallet.getBalance(),
                wallet.getStatus().name(),
                message
        );
    }
}