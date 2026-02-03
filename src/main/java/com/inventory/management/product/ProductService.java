package com.inventory.management.product;

import com.inventory.management.product.ProductEntity;
import com.inventory.management.product.ProductEntity.ProductStatus;
import com.inventory.management.product.ProductEntity.ProductType;
import com.inventory.management.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // ===== OPERACIONES CRUD BÁSICAS =====

    /**
     * Obtiene todos los productos
     */
    public List<ProductEntity> findAll() {
        return productRepository.findAll();
    }

    /**
     * Busca producto por ID
     */
    public Optional<ProductEntity> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Busca producto por código único
     */
    public Optional<ProductEntity> findByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }

    /**
     * Guarda o actualiza un producto
     */
    @Transactional
    public ProductEntity save(ProductEntity product) {
        // Auto-generar createdAt si es nuevo
        if (product.getId() == null) {
            product.setCreatedAt(LocalDateTime.now());
        }
        // Auto-actualizar updatedAt
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    /**
     * Elimina producto por ID
     */
    @Transactional
    public void deleteById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    // ===== BÚSQUEDAS ESPECÍFICAS =====

    //Productos activos

    public List<ProductEntity> findActiveProducts() {
        return productRepository.findByStatus(ProductStatus.ACTIVE);
    }


     //Productos por tipo
    public List<ProductEntity> findByProductType(ProductType productType) {
        return productRepository.findByProductType(productType);
    }

    //Productos por nombre (parcial)
    public List<ProductEntity> findByNameContaining(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    // Productos en rango de precio de venta
    public List<ProductEntity> findBySalePriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findBySalePriceBetween(minPrice, maxPrice);
    }


    //Productos activos caros (precio > mínimo)
    public List<ProductEntity> findExpensiveActiveProducts(BigDecimal minPrice) {
        return productRepository.findByStatusAndSalePriceGreaterThan(ProductStatus.ACTIVE, minPrice);
    }


    //  Conteo total de productos
    public long countAll() {
        return productRepository.count();
    }


    //Conteo por estado (usando consulta nativa)
    public List<Object[]> countByStatus() {
        return productRepository.countByStatusNative();
    }

    // ===== VALIDACIONES DE NEGOCIO =====


    //Verifica si existe código duplicado

    public boolean existsByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode).isPresent();
    }

     //Verifica si código ya existe (excluyendo el ID actual para updates)

    public boolean existsByProductCodeExceptId(String productCode, Long id) {
        Optional<ProductEntity> existing = productRepository.findByProductCode(productCode);
        return existing.isPresent() && !existing.get().getId().equals(id);
    }

    //Actualiza solo el estado del producto
    @Transactional
    public Optional<ProductEntity> updateStatus(Long id, ProductStatus newStatus) {
        return productRepository.findById(id).map(product -> {
            product.setStatus(newStatus);
            product.setUpdatedAt(LocalDateTime.now());
            return productRepository.save(product);
        });
    }

    //Crea producto con valores mínimos requeridos
    @Transactional
    public ProductEntity createProduct(String productCode, String name, ProductType type,
                                       BigDecimal purchasePrice, BigDecimal salePrice) {
        ProductEntity product = new ProductEntity(
                productCode, name, type, null, null, null, null,
                purchasePrice, salePrice, null, null, null,
                ProductStatus.ACTIVE, LocalDateTime.now(), null
        );
        return save(product);
    }
}
