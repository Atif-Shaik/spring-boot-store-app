package com.atifstudios.store.payments;

public class WebhookProcessingException extends RuntimeException {
    public WebhookProcessingException(String message) {
        super(message);
    }
}
