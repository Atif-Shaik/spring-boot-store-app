package com.atifstudios.store.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
    private String orderId;
    private String paymentId;
    private PaymentStatus paymentStatus;
}
