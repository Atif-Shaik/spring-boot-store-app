package com.atifstudios.store.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    /*
        category.id refers to categories table's id
     */
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);
    Product toEntity(AddOrUpdateProductRequest request);
    void update(AddOrUpdateProductRequest request, @MappingTarget Product product);
}
