package com.atifstudios.store.carts;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException() {
        super("cart not found");
    }
}
