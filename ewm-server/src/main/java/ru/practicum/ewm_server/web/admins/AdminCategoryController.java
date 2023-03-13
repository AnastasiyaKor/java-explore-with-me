package ru.practicum.ewm_server.web.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_server.dto.CategoryDto;
import ru.practicum.ewm_server.dto.NewCategoryDto;
import ru.practicum.ewm_server.entity.Category;
import ru.practicum.ewm_server.mapper.CategoryMapper;
import ru.practicum.ewm_server.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Received a request from the administrator to create a category");
        Category category = categoryService.create(CategoryMapper.toCategory(newCategoryDto));
        return CategoryMapper.toCategoryDto(category);
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@RequestBody @Valid NewCategoryDto newCategoryDto, @PathVariable int catId) {
        log.info("received a request from the administrator to change the category by ID: {}", catId);
        Category newCategory = categoryService.update(CategoryMapper.fromNewCategoryDto(newCategoryDto, catId));
        return CategoryMapper.toCategoryDto(newCategory);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteId(@PathVariable int catId) {
        log.info("received a request from the administrator to delete the category under the ID: {}", catId);
        categoryService.getById(catId);
        categoryService.deleteById(catId);
    }
}
