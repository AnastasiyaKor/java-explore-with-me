package ru.practicum.ewm_server.service;

import ru.practicum.ewm_server.entity.Category;

import java.util.List;

public interface CategoryService {
    Category create(Category category);

    Category update(Category category);

    Category getById(int catId);

    void deleteById(int catId);

    List<Category> getCategories(int from, int size);

    Category getCategoryById(int catId);
}
