package ru.practicum.main.categories;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.UpdateCategoryDto;

@RestController
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto createCategory(@RequestBody  @Valid CategoryDto categoryDto) {
        return categoriesService.createCategory(categoryDto);
    }

    @PatchMapping("/admin/categories/{catId}")
    CategoryDto updateCategory(@RequestBody  @Valid UpdateCategoryDto categoryDto,
                               @PathVariable Long catId) {
        return categoriesService.updateCategory(categoryDto, catId);
    }

}
