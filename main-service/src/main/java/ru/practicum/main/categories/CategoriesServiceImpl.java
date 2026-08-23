package ru.practicum.main.categories;

import org.springframework.stereotype.Service;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.repository.CategoriesEntity;
import ru.practicum.main.categories.repository.CategoriesRepository;
import ru.practicum.main.categories.repository.mapper.CategoriesMapper;
import ru.practicum.main.exception.ConflictException;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;

    public CategoriesServiceImpl(CategoriesRepository categoriesRepository, CategoriesMapper categoriesMapper) {
        this.categoriesRepository = categoriesRepository;
        this.categoriesMapper = categoriesMapper;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        CategoriesEntity entity = categoriesMapper.toEntity(categoryDto);
        if(categoriesRepository.existsByNameEqualsIgnoreCase(entity.getName())) {
            throw new ConflictException("Category already exists");
        }
        CategoriesEntity newCategory = categoriesRepository.save(entity);
        return categoriesMapper.toDto(newCategory);
    }

}
