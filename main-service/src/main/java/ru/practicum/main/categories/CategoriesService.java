package ru.practicum.main.categories;

import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.UpdateCategoryDto;

import java.util.Collection;

public interface CategoriesService {

    CategoryDto getCategoryById(Long id);

    Collection<CategoryDto> getAllCategories(int from, int size);

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(UpdateCategoryDto updateCategoryDto, Long id);

}
