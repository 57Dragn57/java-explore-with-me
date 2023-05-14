package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CategoryDto;
import ru.practicum.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public static List<CategoryDto> toCategoryDtoList(List<Category> categories) {
        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }
}
