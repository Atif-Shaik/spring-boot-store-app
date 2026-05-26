package com.atifstudios.store.payments;

import com.atifstudios.store.auth.AuthService;
import com.atifstudios.store.carts.CartService;
import com.atifstudios.store.orders.Order;
import com.atifstudios.store.carts.CartEmptyException;
import com.atifstudios.store.carts.CartNotFoundException;
import com.atifstudios.store.carts.CartRepository;
import com.atifstudios.store.orders.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.isEmpty()) {
           throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());

        String trackingId;
        // create a checkout session
        CheckoutSession checkoutSession = paymentGateway.createCheckoutSession(order); // might throw exception
        trackingId = checkoutSession.getTrackingId();
        order.setTrackingId(trackingId);

        orderRepository.save(order); // saving order in database
        cartService.clearCartItems(cart.getId());
        Long orderID = order.getId();
        int totalAmountInPaise = order.getTotalPrice().multiply(BigDecimal.valueOf(100)).intValue();

        return new CheckoutResponse(orderID, trackingId, totalAmountInPaise, "INR");
    } // method ends

    public HttpStatus handleWebhookEvents(Map<String, String> headers, String payload) {
        // this line may throw exception
        Optional<PaymentResult> paymentResult = paymentGateway.parseWebhookRequest(new WebhookRequest(headers, payload));

        if (paymentResult.isPresent()) {
            PaymentStatus status = paymentResult.get().getPaymentStatus();
            var order = orderRepository.findByTrackingId(paymentResult.get().getOrderId()).orElseThrow();

            switch (status) {
                case PAID -> order.setStatus(PaymentStatus.PAID);
                case FAILED -> order.setStatus(PaymentStatus.FAILED);
            }

            orderRepository.save(order); // update the database
        } // if ends

        return HttpStatus.OK; // data received
    } // method ends

} // class ends
