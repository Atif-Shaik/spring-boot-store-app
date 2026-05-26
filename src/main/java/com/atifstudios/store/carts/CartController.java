package com.atifstudios.store.carts;

import com.atifstudios.store.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    @Operation(summary = "Create a cart.")
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
        // this creates a new cart
        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto); // status 201
    } // method ends

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add a product to the cart.")
    public ResponseEntity<CartItemDto> addToCart(@Parameter(description = "The ID of the cart.") @PathVariable(name = "cartId") UUID cartId, @Valid @RequestBody AddItemToCartRequest request) {
        // this adda an item to the cart (uses request body)
        var cartItemDto = cartService.addToCart(cartId, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto); // status 201
    } // method ends

    @GetMapping("/{cartId}")
    @Operation(summary = "Get the cart.")
    public ResponseEntity<CartDto> getCart(@Parameter(description = "The ID of the cart.") @PathVariable(name = "cartId") UUID cartId) {
        // this gets cart (full info)
        var cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDto); // status 200
    } // method ends

    @PutMapping("/{cartId}/items/{productId}")
    @Operation(summary = "Update product's quantity in the cart.")
    public ResponseEntity<CartItemDto> updateItem(@Parameter(description = "The ID of the cart.") @PathVariable(name = "cartId") UUID cartId, @Parameter(description = "The ID of the product.") @PathVariable(name = "productId") Long productId, @Valid @RequestBody UpdateCartItemRequest request) {
        // this updates the product's quantity in the cart (uses request body)
        var cartItem = cartService.updateCartItemQuantity(cartId, productId, request.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(cartItem); // status 200
    } // class ends

    @DeleteMapping("/{cartId}/items/{productId}")
    @Operation(summary = "Remove a product from the cart.")
    public ResponseEntity<?> removeItem(@Parameter(description = "The ID of the cart.") @PathVariable(name = "cartId") UUID cartId, @Parameter(description = "The ID of the product") @PathVariable(name = "productId") Long productId) {
        // this removes an item from the cart
        cartService.removeCartItem(cartId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // status 204
    } // method ends

    @DeleteMapping("/{cartId}/items")
    @Operation(summary = "Delete products from the cart.")
    public ResponseEntity<?> clearCart(@Parameter(description = "The ID of the cart.") @PathVariable(name = "cartId") UUID cartId) {
        // this clears the entire cart items
        cartService.clearCartItems(cartId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // status 204
    } // method ends

    @DeleteMapping("/{cartId}")
    @Operation(summary = "Delete the cart.")
    public ResponseEntity<?> deleteCart(@Parameter(description = "The ID of the cart.") @PathVariable(name = "cartId") UUID cartId) {
        // this deletes the cart
        cartService.deleteCart(cartId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // status 204
    } // method ends

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "cart not found!")); // status 404
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "product not found"));
    }

} // class ends
