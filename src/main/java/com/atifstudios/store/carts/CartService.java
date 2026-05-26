package com.atifstudios.store.carts;

import com.atifstudios.store.products.ProductNotFoundException;
import com.atifstudios.store.products.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartDto createCart() {
        var cart = new Cart(); // creating cart obj
        cartRepository.save(cart); // saving cart

        return cartMapper.toDto(cart);
    } // method ends

    public CartItemDto addToCart(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }

        var cartItem = cart.addOrUpdateCartItem(product); // updating or adding cart item

        cartRepository.save(cart); // saving cart

        return cartMapper.toDto(cartItem);
    } // method ends

    public CartDto getCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        return cartMapper.toDto(cart);
    } // method ends

    public CartItemDto updateCartItemQuantity(UUID cartId, Long productId, Integer quantity) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        // finding the product
        var cartItem = cart.getCartItemByProductId(productId);

        if (cartItem == null) {
            throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity); // updating quantity (children)
        cartRepository.save(cart); // saving cart (root)

        return cartMapper.toDto(cartItem);
    } // method ends

    public void removeCartItem(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        cart.removeCartItem(productId); // product is removing in Cart class
        cartRepository.save(cart);
    } // method ends

    public void clearCartItems(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        cart.clearCart(); // in cart class
        cartRepository.save(cart);
    } // method ends

    public void deleteCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
           throw new CartNotFoundException();
        }
        cartRepository.delete(cart);
    } // method ends

} // class ends
