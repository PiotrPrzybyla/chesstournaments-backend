package pwr.chesstournamentsbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.chesstournamentsbackend.dto.ResponseMessage;
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
    @DeleteMapping("/{category_id}")
    public ResponseEntity<ResponseMessage> deleteCategory(@PathVariable Integer category_id){
         categoryService.deleteCategory(category_id);
        return new ResponseEntity<>( new ResponseMessage("Category Deleted"), HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{category_id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Integer category_id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(category_id, category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }
}
