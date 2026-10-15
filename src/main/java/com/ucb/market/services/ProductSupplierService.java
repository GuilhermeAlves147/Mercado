package com.ucb.market.services;

import com.ucb.market.dto.LinkSupplierRequest;
import com.ucb.market.dto.ProductSupplierResponse;
import com.ucb.market.exception.BusinessException;
import com.ucb.market.exception.ResourceNotFoundException;
import com.ucb.market.model.Product;
import com.ucb.market.model.ProductSupplier;
import com.ucb.market.model.ProductSupplierId;
import com.ucb.market.model.Supplier;
import com.ucb.market.repository.ProductRepository;
import com.ucb.market.repository.ProductSupplierRepository;
import com.ucb.market.repository.SupplierRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSupplierService {

    private final ProductSupplierRepository productSupplierRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    @Transactional
    public ProductSupplierResponse link(Integer productId, LinkSupplierRequest req) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + productId));
        Supplier supplier = supplierRepository.findById(req.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado: " + req.supplierId()));
        if (productSupplierRepository.existsById(new ProductSupplierId(productId, req.supplierId())))
            throw new BusinessException("Fornecedor já vinculado a este produto");

        ProductSupplier ps = new ProductSupplier();
        ps.setProduct(product);
        ps.setSupplier(supplier);
        ps.setDeliveryTime(req.deliveryTime());
        return ProductSupplierResponse.from(productSupplierRepository.save(ps));
    }
    @Transactional(readOnly = true)
    public List<ProductSupplierResponse> listByProduct(Integer productId) {
        if (!productRepository.existsById(productId))
            throw new ResourceNotFoundException("Produto não encontrado: " + productId);
        return productSupplierRepository.findByIdProductId(productId)
                .stream().map(ProductSupplierResponse::from).toList();
    }

    @Transactional
    public void unlink(Integer productId, Integer supplierId) {
        ProductSupplierId id = new ProductSupplierId(productId, supplierId);
        if (!productSupplierRepository.existsById(id))
            throw new ResourceNotFoundException("Vínculo não encontrado");
        productSupplierRepository.deleteById(id);
    }
}