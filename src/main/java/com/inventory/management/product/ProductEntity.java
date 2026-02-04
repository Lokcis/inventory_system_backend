package com.inventory.management.product;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_code", length = 100, nullable = false, unique = true)
    private String productCode;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "product_type", nullable = false)
    private ProductType productType;

    @Column(name = "size", length = 50)
    private String size;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "location", length = 50)
    private String location;

    @Column(
            name = "purchase_price",
            precision = 10,
            scale = 2,
            nullable = false
    )
    private BigDecimal purchasePrice;

    @Column(
            name = "sale_price",
            precision = 10,
            scale = 2,
            nullable = false
    )
    private BigDecimal salePrice;

    @Column(name = "width", precision = 10, scale = 2)
    private BigDecimal width;

    @Column(name = "height", precision = 10, scale = 2)
    private BigDecimal height;

    @Column(name = "depth", precision = 10, scale = 2)
    private BigDecimal depth;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private ProductStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //Class enum

    public enum ProductType {
        TYPE_A,
        TYPE_B,
        TYPE_C
    }

    public enum ProductStatus {
        ACTIVE,
        INACTIVE,
        DISCONTINUED
    }

}
