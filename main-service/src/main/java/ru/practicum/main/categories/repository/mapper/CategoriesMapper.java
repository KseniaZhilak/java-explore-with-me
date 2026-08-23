package ru.practicum.main.categories.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.UpdateCategoryDto;
import ru.practicum.main.categories.repository.CategoriesEntity;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CategoriesMapper {

    @Mapping(target = "id", ignore = true)
    CategoriesEntity toEntity(CategoryDto categoryDto);

    @Mapping(target = "id", ignore = true)
    CategoriesEntity toEntity(UpdateCategoryDto updateCategoryDto);

    CategoryDto toDto(CategoriesEntity categoriesEntity);

}
