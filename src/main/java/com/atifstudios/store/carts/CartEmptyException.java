package com.atifstudios.store.carts;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException() {
        super("cart is empty");
    }
}
