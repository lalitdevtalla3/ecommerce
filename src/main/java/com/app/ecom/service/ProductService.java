package com.app.ecom.service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.model.Product;
import com.app.ecom.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {



    private final ProductRepository productRepository;

    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest productrequest){

        Product product = new Product();
        updateProductFromRequest(product, productrequest);
        Product savedProduct = productRepository.save(product);
       return mapToProductResponse(savedProduct);

    }

    private ProductResponse mapToProductResponse(Product savedProduct) {

        ProductResponse pr = new ProductResponse();
        pr.setId(savedProduct.getId());
        pr.setName(savedProduct.getName());
        pr.setPrice(savedProduct.getPrice());
        pr.setCategory(savedProduct.getCategory());
        pr.setDescription(savedProduct.getDescription());
        pr.setStockQuantity(savedProduct.getStockQuantity());
        pr.setImageUrl(savedProduct.getImageUrl());
        pr.setActive(savedProduct.isActive());
        return pr;
    }

    private void updateProductFromRequest(Product product, ProductRequest productrequest) {
         product.setName(productrequest.getName());
         product.setPrice(productrequest.getPrice());
         product.setCategory(productrequest.getCategory());
         product.setDescription(productrequest.getDescription());
         product.setStockQuantity(productrequest.getStockQuantity());
         product.setImageUrl(productrequest.getImageUrl());
    }

    public Optional<ProductResponse> updateProduct(Long productId, ProductRequest productrequest) {

       return productRepository.findById(productId).map(
                existingProduct -> {
                    updateProductFromRequest(existingProduct, productrequest);
                    Product savedProduct =  productRepository.save(existingProduct);
                    return mapToProductResponse(savedProduct);
                });

    }

    public List<ProductResponse> getAllProducts() {

        return productRepository.findByActiveTrue().stream()
                .map(this::mapToProductResponse).collect(Collectors.toList());

    }
    public boolean deleteProductBytId(Long id){

                return productRepository.findById(id).map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);

    }

    public List<ProductResponse> searchProducts(String keyword) {

        return productRepository.searchProducts(keyword).stream().map(this::mapToProductResponse).collect(Collectors.toList());

    }
}

