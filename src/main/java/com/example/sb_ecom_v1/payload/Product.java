package com.example.sb_ecom_v1.payload;

import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String productName;
    private String description;
    private Integer quantity;
    private double price;
    private double specialPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
