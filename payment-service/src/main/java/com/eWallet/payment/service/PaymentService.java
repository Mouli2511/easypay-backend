package com.eWallet.payment.service;

import com.eWallet.payment.dto.PaymentRequest;
import com.eWallet.payment.dto.PaymentResponse;
import com.eWallet.payment.entity.Payment;
import com.eWallet.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentResponse processPayment(String payerEmail, PaymentRequest request) {
        Payment payment = new Payment();
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPayerEmail(payerEmail);
        payment.setMerchantName(request.getMerchantName());
        payment.setMerchantId(request.getMerchantId());
        payment.setAmount(request.getAmount());
        payment.setDescription(request.getDescription());
        payment.setPaymentMethod(
                Payment.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));

        // Mock payment processing — always succeeds for demo
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(payment);
        return toResponse(payment, "Payment processed successfully!");
    }

    public PaymentResponse getPaymentStatus(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found!"));
        return toResponse(payment, "Success");
    }

    public PaymentResponse refundPayment(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found!"));

        if (payment.getStatus() != Payment.PaymentStatus.SUCCESS) {
            throw new RuntimeException("Only successful payments can be refunded!");
        }

        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        return toResponse(payment, "Refund processed successfully!");
    }

    public List<PaymentResponse> getPaymentHistory(String payerEmail) {
        return paymentRepository
                .findByPayerEmailOrderByCreatedAtDesc(payerEmail)
                .stream()
                .map(p -> toResponse(p, "Success"))
                .collect(Collectors.toList());
    }

    private PaymentResponse toResponse(Payment payment, String message) {
        return new PaymentResponse(
                payment.getId(),
                payment.getTransactionId(),
                payment.getPayerEmail(),
                payment.getMerchantName(),
                payment.getAmount(),
                payment.getStatus().name(),
                payment.getPaymentMethod().name(),
                message
        );
    }
}