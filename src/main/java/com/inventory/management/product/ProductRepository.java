package com.inventory.management.product;

import com.inventory.management.product.ProductEntity;
import com.inventory.management.product.ProductEntity.ProductStatus;
import com.inventory.management.product.ProductEntity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    // Buscar por código único (VARCHAR UNIQUE)
    Optional<ProductEntity> findByProductCode(String productCode);

    // Buscar por nombre (contiene, ignorando mayúsculas)
    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    List<ProductEntity> findByStatus(ProductStatus status);

    // Buscar por tipo de producto (enum anidado)
    List<ProductEntity> findByProductType(ProductType productType);

    // Buscar por rango de precios de venta
    List<ProductEntity> findBySalePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Productos activos con precio de venta > mínimo
    List<ProductEntity> findByStatusAndSalePriceGreaterThan(ProductStatus status, BigDecimal minPrice);

    // Consulta personalizada: productos activos por precio
    @Query("SELECT p FROM ProductEntity p WHERE p.status = :status AND p.salePrice > :minPrice")
    List<ProductEntity> findActiveProductsAbovePrice(@Param("status") ProductStatus status,
                                                     @Param("minPrice") BigDecimal minPrice);

    // Conteo por estado (consulta nativa)
    @Query(value = "SELECT status, COUNT(*) FROM products GROUP BY status", nativeQuery = true)
    List<Object[]> countByStatusNative();

}
