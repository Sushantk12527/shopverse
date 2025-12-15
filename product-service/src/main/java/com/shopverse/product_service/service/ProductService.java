package com.shopverse.product_service.service;

import com.shopverse.product_service.model.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product updateProduct(Long id,Product updatedProduct);
    Product getProductBYId(Long id);
    List<Product> getAllProducts();
    void deleteProduct(Long id);
}
