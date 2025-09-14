package com.example.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Represents a product in the system.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(value = "product")
public class Product {
    @Id
    private String id; // Unique identifier for the product
    private String name; // Name of the product
    private String description; // Description of the product
    private BigDecimal price; // Price of the product
}
