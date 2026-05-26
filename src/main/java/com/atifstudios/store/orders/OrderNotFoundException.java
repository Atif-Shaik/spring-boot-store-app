package com.atifstudios.store.orders;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("order not found");
    }
}
