package com.atifstudios.store.payments;

import com.atifstudios.store.carts.CartEmptyException;
import com.atifstudios.store.carts.CartNotFoundException;
import com.atifstudios.store.common.ErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
@Tag(name = "Checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    @Operation(summary = "Checkout cart (Razorpay).")
    public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        // this is checkout api
        var checkoutResponse = checkoutService.checkout(request);
        return ResponseEntity.status(HttpStatus.OK).body(checkoutResponse);
    } // method ends

    @PostMapping("/webhook")
    @Operation(summary = "Webhook endpoint (for server-to-server).")
    public ResponseEntity<Void> webhook(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
       // this is webhook, it is for payment gateway's server
       HttpStatus httpStatus = checkoutService.handleWebhookEvents(headers, payload);
       return ResponseEntity.status(httpStatus).build();
    } // method ends

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleCartNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(InvalidWebhookSignatureException.class)
    public ResponseEntity<?> handleInvalidWebhookSignature(Exception e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(WebhookProcessingException.class)
    public ResponseEntity<?> handleWebhookException(Exception e) {
        log.error("SYSTEM CRASH: Webhook processing failed due to {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

} // class ends
