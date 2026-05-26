package com.atifstudios.store.products;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> getProducts(Byte categoryId) {
        List<Product> products;

        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
            if (products.isEmpty()) {
                throw new ProductNotFoundException();
            }
        } else {
            products = productRepository.findAllWithCategory();
        }

        return products.stream().map(productMapper::toDto).toList();
    } // method ends

    public ProductDto getProduct(Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        return productMapper.toDto(product);
    } // method ends

    public ProductDto addNewProduct(AddOrUpdateProductRequest request) {
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (category == null) {
            throw new CategoryNotFoundException();
        }
        var product = productMapper.toEntity(request);
        product.setCategory(category);
        productRepository.save(product);

        return productMapper.toDto(product);
    } // method ends

    public ProductDto updateProduct(AddOrUpdateProductRequest request, Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }

        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (category == null) {
            throw new CategoryNotFoundException();
        }
        productMapper.update(request, product); // updating the product
        product.setCategory(category); // setting the category
        productRepository.save(product); // saving in database
        return productMapper.toDto(product);
    } // class ends

    public void deleteTheProduct(Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        productRepository.delete(product);
    } // method ends

} // class ends
