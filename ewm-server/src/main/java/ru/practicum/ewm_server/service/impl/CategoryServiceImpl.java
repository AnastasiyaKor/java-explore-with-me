package ru.practicum.ewm_server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_server.entity.Category;
import ru.practicum.ewm_server.exceptions.ConflictException;
import ru.practicum.ewm_server.exceptions.NotFoundException;
import ru.practicum.ewm_server.repository.CategoryRepository;
import ru.practicum.ewm_server.repository.EventRepository;
import ru.practicum.ewm_server.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        Category updateCategory = getById(category.getId());
        updateCategory.setName(category.getName());
        categoryRepository.save(updateCategory);
        return updateCategory;
    }

    @Override
    public Category getById(int catId) {
        return categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Категория не найдена или недоступна"));
    }


    @Override
    public void deleteById(int catId) {
        categoryRepository.findById(catId);
        List<Integer> categoryId = eventRepository.findAllByCategoryId(catId);
        if (categoryId.isEmpty()) {
            categoryRepository.deleteById(catId);
        } else {
            throw new ConflictException("Существуют события, связанные с категорией");
        }
    }

    @Override
    public List<Category> getCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable).getContent();
    }

    @Override
    public Category getCategoryById(int catId) {
        return getById(catId);
    }

}
