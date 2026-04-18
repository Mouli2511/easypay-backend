package com.eWallet.payment.controller;

import com.eWallet.payment.dto.PaymentRequest;
import com.eWallet.payment.dto.PaymentResponse;
import com.eWallet.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Payment Service is UP!");
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(
            Authentication auth,
            @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(
                paymentService.processPayment(auth.getName(), request));
    }

    @GetMapping("/status/{transactionId}")
    public ResponseEntity<PaymentResponse> getStatus(
            @PathVariable String transactionId) {
        return ResponseEntity.ok(
                paymentService.getPaymentStatus(transactionId));
    }

    @PostMapping("/refund/{transactionId}")
    public ResponseEntity<PaymentResponse> refund(
            @PathVariable String transactionId) {
        return ResponseEntity.ok(
                paymentService.refundPayment(transactionId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<PaymentResponse>> getHistory(Authentication auth) {
        return ResponseEntity.ok(
                paymentService.getPaymentHistory(auth.getName()));
    }
}