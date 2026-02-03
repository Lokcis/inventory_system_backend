package com.inventory.management.product;

import com.inventory.management.product.ProductEntity;
import com.inventory.management.product.ProductEntity.ProductStatus;
import com.inventory.management.product.ProductEntity.ProductType;
import com.inventory.management.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ===== CRUD BÁSICO =====

    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity product) {
        ProductEntity saved = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable Long id, @RequestBody ProductEntity product) {
        return productService.findById(id)
                .map(existing -> {
                    product.setId(id);
                    ProductEntity updated = productService.save(product);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.findById(id).isPresent()) {
            productService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ===== BÚSQUEDAS ESPECÍFICAS =====

    @GetMapping("/code/{productCode}")
    public ResponseEntity<ProductEntity> getByProductCode(@PathVariable String productCode) {
        return productService.findByProductCode(productCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductEntity>> getActiveProducts() {
        return ResponseEntity.ok(productService.findActiveProducts());
    }

    @GetMapping("/type/{productType}")
    public ResponseEntity<List<ProductEntity>> getByProductType(@PathVariable ProductType productType) {
        return ResponseEntity.ok(productService.findByProductType(productType));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductEntity>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.findByNameContaining(name));
    }

    @GetMapping("/price")
    public ResponseEntity<List<ProductEntity>> getByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return ResponseEntity.ok(productService.findBySalePriceBetween(min, max));
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<ProductEntity> updateStatus(
            @PathVariable Long id,
            @PathVariable ProductStatus status) {
        return productService.updateStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/quick")
    public ResponseEntity<ProductEntity> createQuickProduct(
            @RequestParam String productCode,
            @RequestParam String name,
            @RequestParam int productTypeOrdinal,
            @RequestParam BigDecimal purchasePrice,
            @RequestParam BigDecimal salePrice) {

        ProductType type = ProductType.values()[productTypeOrdinal];
        ProductEntity product = productService.createProduct(
                productCode, name, type, purchasePrice, salePrice
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats() {
        long total = productService.countAll();
        List<Object[]> statusCount = productService.countByStatus();
        return ResponseEntity.ok(Map.of("total", total, "statusCount", statusCount));
    }

}
