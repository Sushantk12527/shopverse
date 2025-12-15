package com.shopverse.product_service.service;

import com.shopverse.product_service.event.ProductEventProducer;
import com.shopverse.product_service.model.Product;
import com.shopverse.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final ProductEventProducer productEventProducer;

    public ProductServiceImpl(ProductRepository productRepository, ProductEventProducer productEventProducer){
        this.productRepository=productRepository;
        this.productEventProducer = productEventProducer;
    }

    @Override
    public Product createProduct(Product product) {
        Product savedProduct= productRepository.save(product);
        //publish event
        productEventProducer.sendProductCreatedEvent(product);
        return product;
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {

        Product existingProduct=productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product Not Found"));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());

        Product savedProduct=productRepository.save(existingProduct);

        productEventProducer.sendProductUpdatedEvent(savedProduct);

        return savedProduct;
    }

    @Override
    public Product getProductBYId(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Product not found with id: "+id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()->new RuntimeException("Product Not Found"));

        productRepository.delete(product);

        //Send delete event
        productEventProducer.sendProductDeleteEvent(id);
        System.out.println("Product deleted and event sent: "+ id);


    }
}
