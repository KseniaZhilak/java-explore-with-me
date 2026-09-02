package ru.practicum.main.categories;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.categories.dto.CategoryDto;
import ru.practicum.main.categories.dto.UpdateCategoryDto;
import ru.practicum.main.categories.repository.CategoriesEntity;
import ru.practicum.main.categories.repository.CategoriesRepository;
import ru.practicum.main.categories.repository.mapper.CategoriesMapper;
import ru.practicum.main.events.repository.EventsRepository;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;
    private final EventsRepository eventsRepository;

    public CategoriesServiceImpl(CategoriesRepository categoriesRepository, CategoriesMapper categoriesMapper,
                                 EventsRepository eventsRepository) {
        this.categoriesRepository = categoriesRepository;
        this.categoriesMapper = categoriesMapper;
        this.eventsRepository = eventsRepository;
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        return categoriesMapper.toDto(
                categoriesRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Category not found"))
        );
    }

    @Override
    public Collection<CategoryDto> getAllCategories(int offset, int size) {
        List<CategoriesEntity> all = categoriesRepository.findAll(offset, size);
        return all.stream().map(categoriesMapper::toDto).toList();
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        CategoriesEntity entity = categoriesMapper.toEntity(categoryDto);
        if (categoriesRepository.existsByNameEqualsIgnoreCase(entity.getName())) {
            throw new ConflictException("Category already exists");
        }
        CategoriesEntity newCategory = categoriesRepository.save(entity);
        return categoriesMapper.toDto(newCategory);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(UpdateCategoryDto updateCategoryDto, Long id) {
        Optional<CategoriesEntity> categories = categoriesRepository.findById(id);
        if (categories.isEmpty()) {
            throw new NotFoundException("Category not found");
        }
        if (categoriesRepository.existsByNameEqualsIgnoreCaseAndIdNot(updateCategoryDto.getName(), id)) {
            throw new ConflictException("Category already exists");
        }

        CategoriesEntity entity = categoriesMapper.toUpdatedEntity(updateCategoryDto, categories.get());
        return categoriesMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoriesRepository.existsById(id)) {
            throw new NotFoundException("Category not found");
        }
        if (eventsRepository.existsByCategoryId(id)) {
            throw new ConflictException("Category is used by events");
        }
        categoriesRepository.deleteById(id);
    }

}
