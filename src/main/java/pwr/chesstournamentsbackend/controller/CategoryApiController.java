package pwr.chesstournamentsbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.chesstournamentsbackend.model.Category;
import pwr.chesstournamentsbackend.model.User;
import pwr.chesstournamentsbackend.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryApiController {
    public final CategoryService categoryService;

    public CategoryApiController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{category_id}")
    public ResponseEntity<Category> getUserById(@PathVariable Integer category_id) {
        return categoryService.findById(category_id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.saveCategory(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }
}
