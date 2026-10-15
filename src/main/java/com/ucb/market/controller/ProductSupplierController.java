package com.ucb.market.controller;

import com.ucb.market.dto.LinkSupplierRequest;
import com.ucb.market.dto.ProductSupplierResponse;
import com.ucb.market.services.ProductSupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/{productId}/suppliers")
@RequiredArgsConstructor
public class ProductSupplierController {

    private final ProductSupplierService productSupplierService;

    @GetMapping
    public List<ProductSupplierResponse> list(@PathVariable Integer productId) {
        return productSupplierService.listByProduct(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductSupplierResponse link(@PathVariable Integer productId,
                                        @RequestBody @Valid LinkSupplierRequest req) {
        return productSupplierService.link(productId, req);
    }

    @DeleteMapping("/{supplierId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlink(@PathVariable Integer productId, @PathVariable Integer supplierId) {
        productSupplierService.unlink(productId, supplierId);
    }
}
