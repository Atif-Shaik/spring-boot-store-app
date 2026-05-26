package com.atifstudios.store.products;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddOrUpdateProductRequest {
    @NotBlank(message = "Name of the product must be provided!")
    private String name;

    @NotNull(message = "Price of the product must be provided!")
    @DecimalMin(value = "1.00", message = "Price must be grater than zero!")
    private BigDecimal price;

    private String description;

    @NotNull(message = "Category ID must be provided!")
    @Min(value = 1, message = "Category ID must be grater than zero!")
    private Byte categoryId;
}
