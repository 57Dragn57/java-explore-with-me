package ru.practicum.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.repositories.CategoryRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CategoryService {
    private CategoryRepository categoryRepository;

    public List<CategoryDto> getCategories(int from, int size) {
        List<CategoryDto> catDto = CategoryMapper.toCategoryDtoList(categoryRepository.findAll(PageRequest.of(from / size, size)).toList());

        if (catDto.isEmpty()) {
            return List.of();
        }

        return catDto;
    }

    public CategoryDto getCategory(long catId) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found")));
    }
}
