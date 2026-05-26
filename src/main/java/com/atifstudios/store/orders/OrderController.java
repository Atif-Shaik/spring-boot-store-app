package com.atifstudios.store.orders;

import com.atifstudios.store.common.ErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
@Tag(name = "Order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get all orders.")
    public List<OrderDto> getAllOrders() {
        // this gets all orders
        return orderService.getAllOrders();
    } // method ends

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID.")
    public ResponseEntity<OrderDto> getOrder(@Parameter(description = "The ID of the order.") @PathVariable(name = "orderId") Long orderId) {
        var orderDto = orderService.getOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    } // method ends

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> handleOrderNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDenied(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(e.getMessage()));
    }

} // class ends
