package com.app.ecom.service;

import com.app.ecom.dto.OrderResponse;
import com.app.ecom.model.*;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    public Optional<OrderResponse> createOrder(String userId){

        //validation for cart items
        List<CartItem> cartItems = cartService.getCartItems(userId);
        if(cartItems.isEmpty()){
            return Optional.empty();
        }
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if(userOptional.isEmpty()){
                return Optional.empty();
        }
        User user = userOptional.get();

        BigDecimal totalPrice = cartItems.stream().map(cartItem -> cartItem.getPrice())
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        //create order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream().
                map(item -> new OrderItem(
                        null ,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                )).toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);
        return Optional.of(mapToOrderResponse(savedOrder));
    }


    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream().map(item -> new OrderItemDto(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice().multiply(new BigDecimal((item.getQuantity())))
                )).toList(),
                order.getOrderDate()
        );
    }
}
