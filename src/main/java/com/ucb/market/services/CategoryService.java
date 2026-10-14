package com.ucb.market.services;

import com.ucb.market.dto.CategoryRequest;
import com.ucb.market.dto.CategoryResponse;
import com.ucb.market.exception.BusinessException;
import com.ucb.market.exception.ResourceNotFoundException;
import com.ucb.market.model.Category;
import com.ucb.market.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse create(CategoryRequest req) {
        Category c = new Category();
        fill(c, req);
        return CategoryResponse.from(categoryRepository.save(c));
    }

    @Transactional
    public CategoryResponse update(Integer id, CategoryRequest req) {
        Category c = find(id);
        if (id.equals(req.parentId()))
            throw new BusinessException("Uma categoria não pode ser pai dela mesma");
        fill(c, req);
        return CategoryResponse.from(c);
    }

    private void fill(Category c, CategoryRequest req) {
        c.setName(req.name());
        c.setDescription(req.description());
        c.setParent(req.parentId() == null ? null : find(req.parentId()));
    }

    private Category find(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + id));
    }
    @Transactional(readOnly = true)
    public List<CategoryResponse> list(Integer parentId) {
        List<Category> found = parentId == null
                ? categoryRepository.findAll()
                : categoryRepository.findByParentId(parentId);
        return found.stream().map(CategoryResponse::from).toList();
    }

    public void delete(Integer id) {
        find(id);
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("A categoria possui subcategorias ou produtos e não pode ser excluída");
        }
    }
}

