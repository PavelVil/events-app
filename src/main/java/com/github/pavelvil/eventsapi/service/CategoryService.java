package com.github.pavelvil.eventsapi.service;

import com.github.pavelvil.eventsapi.entity.Category;
import com.github.pavelvil.eventsapi.exception.EntityNotFoundException;
import com.github.pavelvil.eventsapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Category with id {0} not found!", id)
                ));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Set<Category> upsertCategories(Set<Category> categories) {
        Set<String> eventCategories = categories.stream()
                .map(Category::getName).collect(Collectors.toSet());

        List<Category> existedCategories = categoryRepository.findAllByNameIn(eventCategories);
        Set<String> existedCategoryNames = existedCategories.stream()
                .map(Category::getName)
                .collect(Collectors.toSet());

        List<Category> categoriesForUpdate = categories.stream()
                .filter(it -> !existedCategoryNames.contains(it.getName()))
                .toList();

        return Stream.concat(existedCategories.stream(),
                        categoryRepository.saveAll(categoriesForUpdate).stream())
                .collect(Collectors.toSet());
    }

}
