package ru.practicum.main.categories;

import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.UpdateCategoryDto;

public interface CategoriesService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(UpdateCategoryDto updateCategoryDto, Long id);

}
