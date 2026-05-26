package com.atifstudios.store.payments;

import lombok.Data;

@Data
public class CheckoutResponse {
    private Long orderId;
    private String trackingId;
    private int amountInPaise;
    private String currency;

    public CheckoutResponse(Long orderId, String trackingId, int amountInPaise, String currency) {
        this.orderId = orderId;
        this.trackingId = trackingId;
        this.amountInPaise = amountInPaise;
        this.currency = currency;
    }
}
