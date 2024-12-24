package org.example.controller;

import org.example.entity.Product;
import org.example.entity.ProductCategory;
import org.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductGraphQLController {

    private final ProductService productService;

    @QueryMapping
    public Product product(@Argument Long id) {
        return productService.getProduct(id);
    }

    @QueryMapping
    public List<Product> products() {
        return productService.getAllProducts();
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument ProductCategory category) {
        return productService.getProductsByCategory(category);
    }

    @MutationMapping
    public Product createProduct(@Argument Product product) {
        return productService.createProduct(product);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument Product product) {
        return productService.updateProduct(id, product);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        productService.deleteProduct(id);
        return true;
    }
}