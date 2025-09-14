package com.example.product.repository;

import com.example.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    // This interface will automatically provide CRUD operations for Product entities
    // Additional custom query methods can be defined here if needed
}
