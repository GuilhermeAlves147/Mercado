package com.ucb.market.controller;

import com.ucb.market.dto.CategoryRequest;
import com.ucb.market.dto.CategoryResponse;
import com.ucb.market.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> list(@RequestParam(required = false) Integer parentId) {
        return categoryService.list(parentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@RequestBody @Valid CategoryRequest req) {
        return categoryService.create(req);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Integer id, @RequestBody @Valid CategoryRequest req) {
        return categoryService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        categoryService.delete(id);
    }
}