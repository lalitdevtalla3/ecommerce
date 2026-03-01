package com.app.ecom.controller;

import com.app.ecom.dto.OrderResponse;
import com.app.ecom.model.Order;
import com.app.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderservice;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader("X-User-Id") String userId){

       return orderservice.createOrder(userId)
               .map(orderResponse -> new ResponseEntity<>(orderResponse, HttpStatus.CREATED))
                               .orElseGet(() -> ResponseEntity.badRequest().build());

    }
}
