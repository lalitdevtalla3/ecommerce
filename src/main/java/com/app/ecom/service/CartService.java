package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public boolean addToCart(String userId, CartItemRequest request) {

        // Look For Product
       Product productOpt =productRepository.findById(request.getProductId()).orElseThrow(() -> new RuntimeException("Product Not Found"));
       User userOpt = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("User Not Found"));
        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(userOpt, productOpt);
        int totalQuantity = (existingCartItem != null) ? existingCartItem.getQuantity() + request.getQuantity() : request.getQuantity();
        if (productOpt.getStockQuantity() < totalQuantity) {
            return false; // Out of stock
        }
        if (existingCartItem != null) {
            // update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(productOpt.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        } else {
            // create a new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUser(userOpt);
            cartItem.setProduct(productOpt);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(productOpt.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteFromCart(String userId, Long productId) {
       Product productOpt = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product Not Found"));
      User userOpt = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("User Not Found"));
        cartItemRepository.deleteByUserAndProduct(userOpt, productOpt);
        return false;
    }

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    public List<CartItem> getCartItems(String userId) {
        return userRepository.findById(Long.valueOf(userId)).map(cartItemRepository::findByUser).orElse(List.of());
    }

    public void clearCart(Long userId) {

      userRepository.findById(userId).ifPresent(cartItemRepository::deleteByUser);
    }
}
