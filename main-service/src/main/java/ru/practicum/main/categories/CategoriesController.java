package ru.practicum.main.categories;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.UpdateCategoryDto;

import java.util.Collection;

@RestController
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoriesService.createCategory(categoryDto);
    }

    @PatchMapping("/admin/categories/{catId}")
    CategoryDto updateCategory(@RequestBody @Valid UpdateCategoryDto categoryDto,
                               @PathVariable Long catId) {
        return categoriesService.updateCategory(categoryDto, catId);
    }

    @GetMapping("/categories")
    Collection<CategoryDto> getAllCategories(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return categoriesService.getAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    CategoryDto getCategory(@PathVariable Long catId) {
        return categoriesService.getCategoryById(catId);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable Long catId) {
        categoriesService.deleteCategory(catId);
    }

}
