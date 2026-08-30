package ru.practicum.main.categories.repository.mapper;

import org.mapstruct.*;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.UpdateCategoryDto;
import ru.practicum.main.categories.repository.CategoriesEntity;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CategoriesMapper {

    @Mapping(target = "id", ignore = true)
    CategoriesEntity toEntity(CategoryDto categoryDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    CategoriesEntity toUpdatedEntity(UpdateCategoryDto updateCategoryDto, @MappingTarget CategoriesEntity entity);

    CategoryDto toDto(CategoriesEntity categoriesEntity);

}
