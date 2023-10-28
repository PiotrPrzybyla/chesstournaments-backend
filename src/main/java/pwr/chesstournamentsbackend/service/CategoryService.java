package pwr.chesstournamentsbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.chesstournamentsbackend.model.Category;
import pwr.chesstournamentsbackend.repository.CategoryRepository;

import java.util.Optional;
@Service

public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }
    public void deleteCategory(Integer id){
        categoryRepository.deleteById(id);
    }
    public Category updateCategory(Integer id, Category category) {
        if (categoryRepository.existsById(id)) {
            category.setCategoryId(id);
            return categoryRepository.save(category);
        } else {
            throw new EntityNotFoundException("Category not found with id " + id);
        }
    }

}
