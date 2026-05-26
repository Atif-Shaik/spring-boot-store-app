package com.atifstudios.store.carts;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotNull(message = "Product ID must be provided!")
    @Min(value = 1, message = "Product ID must be grater than zero!")
    private Long productId;
}
