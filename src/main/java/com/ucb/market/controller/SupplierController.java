package com.ucb.market.controller;

import com.ucb.market.dto.SupplierRequest;
import com.ucb.market.dto.SupplierResponse;
import com.ucb.market.services.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public List<SupplierResponse> list() { return supplierService.list(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierResponse create(@RequestBody @Valid SupplierRequest req) {
        return supplierService.create(req);
    }

    @PutMapping("/{id}")
    public SupplierResponse update(@PathVariable Integer id, @RequestBody @Valid SupplierRequest req) {
        return supplierService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) { supplierService.delete(id); }
}
