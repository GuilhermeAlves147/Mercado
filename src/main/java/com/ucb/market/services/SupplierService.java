package com.ucb.market.services;

import com.ucb.market.dto.SupplierRequest;
import com.ucb.market.dto.SupplierResponse;
import com.ucb.market.exception.BusinessException;
import com.ucb.market.exception.ResourceNotFoundException;
import com.ucb.market.model.Supplier;
import com.ucb.market.repository.SupplierRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Transactional
    public SupplierResponse create(SupplierRequest req) {
        if (supplierRepository.existsByCnpj(req.cnpj()))
            throw new BusinessException("CNPJ já cadastrado");
        Supplier s = new Supplier();
        fill(s, req);
        return SupplierResponse.from(supplierRepository.save(s));
    }

    @Transactional
    public SupplierResponse update(Integer id, SupplierRequest req) {
        Supplier s = find(id);
        fill(s, req);
        return SupplierResponse.from(s);
    }

    private void fill(Supplier s, SupplierRequest req) {
        s.setName(req.name());
        s.setCnpj(req.cnpj());
        s.setPhone(req.phone());
    }

    private Supplier find(Integer id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<SupplierResponse> list() {
        return supplierRepository.findAll().stream().map(SupplierResponse::from).toList();
    }

    public void delete(Integer id) {
        find(id);
        try {
            supplierRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("O fornecedor está vinculado a produtos e não pode ser excluído");
        }
    }
}
