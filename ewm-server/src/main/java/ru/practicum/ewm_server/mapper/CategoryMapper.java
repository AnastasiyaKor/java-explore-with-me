package ru.practicum.ewm_server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_server.dto.CategoryDto;
import ru.practicum.ewm_server.dto.NewCategoryDto;
import ru.practicum.ewm_server.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryMapper {

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public static Category fromNewCategoryDto(NewCategoryDto newCategoryDto, int catId) {
        return Category.builder()
                .id(catId)
                .name(newCategoryDto.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static List<CategoryDto> toCategoryDtos(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}
