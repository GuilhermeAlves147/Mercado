package com.ucb.market.services;

import com.ucb.market.dto.ProductDTO;
import com.ucb.market.model.Product;
import com.ucb.market.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public List<Product> listProducts() {

        return productRepository.findAll();
    }
    public Product findById(int id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));
    }
    public Product create(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setValue(dto.getValue());
        product.setQuantity(dto.getQuantity());

        return productRepository.save(product);
    }
    public void delete(int id){
        if(!productRepository.existsById(id)){
            throw new RuntimeException("produto n existe");
        }
        productRepository.deleteById(id);
    }
    public Product update ( int id,ProductDTO dto){
        Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Produto não encontrado id: " + id));

        product.setName(dto.getName());
        product.setValue(dto.getValue());
        product.setQuantity(dto.getQuantity());

        return productRepository.save(product);
    }
}
