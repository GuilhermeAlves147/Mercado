package com.ucb.market.controller;

import com.ucb.market.dto.ProductDTO;
import com.ucb.market.model.Product;
import com.ucb.market.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    public List<Product> listProducts() {
        return productService.listProducts();
    }
    @GetMapping("{id}")
    public Product findById(@PathVariable int id){
        return productService.findById(id);
    }
    @PostMapping
    public Product create(@RequestBody @Valid ProductDTO productDTO){
        return productService.create(productDTO);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete (@PathVariable int id){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("{id}")
    public Product update(@PathVariable int id,@RequestBody @Valid ProductDTO productDTO){
        return productService.update(id,productDTO);
    }
}
