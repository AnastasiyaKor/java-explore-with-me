package ru.practicum.ewm_server.web.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_server.dto.CategoryDto;
import ru.practicum.ewm_server.mapper.CategoryMapper;
import ru.practicum.ewm_server.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Received a public request for categories");
        return CategoryMapper.toCategoryDtos(categoryService.getCategories(from, size));
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable int catId) {
        log.info("Received a public request to get a category under the id: {}", catId);
        return CategoryMapper.toCategoryDto(categoryService.getCategoryById(catId));
    }

}
