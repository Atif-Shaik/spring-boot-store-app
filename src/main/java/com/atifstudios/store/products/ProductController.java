package com.atifstudios.store.products;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
@Tag(name = "Products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get products.")
    public List<ProductDto> getAllProducts(@Parameter(description = "Category ID (Optional).") @RequestParam(required = false, name = "categoryId") Byte categoryId) {
        // this gets all products Or gets a product by category id using params (e.g, "/products?categoryId=1")
        return productService.getProducts(categoryId);
    } // method ends

    @GetMapping("/{id}")
    @Operation(summary = "Get a product.")
    public ProductDto getProduct(@Parameter(description = "The ID of the product.") @PathVariable(name = "id") Long id) {
         // this gets a product by its id
         return productService.getProduct(id);
    } // method ends

    @PostMapping
    @Operation(summary = "Add a product.")
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody AddOrUpdateProductRequest request) {
        // this adds a new product
        var productDto = productService.addNewProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto); // status 201
    } // method ends

    @PutMapping("/{id}")
    @Operation(summary = "Update the product.")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody AddOrUpdateProductRequest request, @Parameter(description = "The ID of the product.") @PathVariable(name = "id") Long id) {
       // this updates the products
       var productDto = productService.updateProduct(request, id);
       return ResponseEntity.status(HttpStatus.OK).body(productDto); // status 200
    } // method ends

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the product.")
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "The ID of the product") @PathVariable(name = "id") Long id) {
        // this deletes the product
        productService.deleteTheProduct(id);
        return ResponseEntity.noContent().build(); // status 204
    } // method ends

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "product not found")); // status 404
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoryNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "category not found")); // status 400
    }
} // class ends
