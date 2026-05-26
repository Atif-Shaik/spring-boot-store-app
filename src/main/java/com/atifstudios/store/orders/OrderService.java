package com.atifstudios.store.orders;

import com.atifstudios.store.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(user);

        return orders.stream().map(orderMapper::toDto).toList();
    } // method ends

    public OrderDto getOrder(Long orderId) {
        var order = orderRepository.getOrderWithItems(orderId).orElseThrow(OrderNotFoundException::new);

        var user = authService.getCurrentUser();
        if (!order.isPlacedBy(user.getId())) {
            throw new AccessDeniedException("you don't have access to this order");
        }

        return orderMapper.toDto(order);
    } // method ends

} // class ends
