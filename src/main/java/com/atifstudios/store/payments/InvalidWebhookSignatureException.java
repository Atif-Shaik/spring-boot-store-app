package com.atifstudios.store.payments;

public class InvalidWebhookSignatureException extends RuntimeException {
    public InvalidWebhookSignatureException() {
        super("CRITICAL: Webhook signature validation failed! Potential fraud attempt.");
    }
}
