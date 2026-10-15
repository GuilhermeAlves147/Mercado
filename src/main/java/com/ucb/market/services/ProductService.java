package com.ucb.market.services;

import com.ucb.market.dto.ProductRequest;
import com.ucb.market.dto.ProductResponse;
import com.ucb.market.exception.BusinessException;
import com.ucb.market.exception.ResourceNotFoundException;
import com.ucb.market.model.Product;
import com.ucb.market.repository.CategoryRepository;
import com.ucb.market.repository.ProductRepository;
import com.ucb.market.repository.SellerRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductResponse create(ProductRequest req) {
        Product p = new Product();
        fill(p, req);
        return ProductResponse.from(productRepository.save(p));
    }

    @Transactional
    public ProductResponse update(Integer id, ProductRequest req) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + id));
        fill(p, req);
        return ProductResponse.from(p);
    }

    private void fill(Product p, ProductRequest req) {
        p.setName(req.name());
        p.setValue(req.value());
        p.setQuantity(req.quantity());
        p.setSeller(sellerRepository.findById(req.sellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor não encontrado: " + req.sellerId())));
        p.setCategory(categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + req.categoryId())));
    }
    @Transactional(readOnly = true)
    public List<ProductResponse> list(Integer categoryId, Integer sellerId, String name) {
        List<Product> found;
        if (categoryId != null)    found = productRepository.findByCategoryId(categoryId);
        else if (sellerId != null) found = productRepository.findBySellerId(sellerId);
        else if (name != null)     found = productRepository.findByNameContainingIgnoreCase(name);
        else                       found = productRepository.findAll();
        return found.stream().map(ProductResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Integer id) {
        return productRepository.findById(id).map(ProductResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + id));
    }

    public void delete(Integer id) {
        if (!productRepository.existsById(id))
            throw new ResourceNotFoundException("Produto não encontrado: " + id);
        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Produto já está em pedidos ou carrinhos e não pode ser excluído");
        }
    }
}