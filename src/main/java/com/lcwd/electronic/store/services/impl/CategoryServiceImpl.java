package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.UUID;


@Service
public class CategoryServiceImpl implements CategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ModelMapper mapper;
    @Override
    public CategoryDto create(@Valid CategoryDto categoryDto) {

        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);

        Category category = mapper.map(categoryDto,Category.class);
        Category savedCategory = repository.save(category);
        CategoryDto dto =  mapper.map(savedCategory,CategoryDto.class);
        return dto;
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {

        //get category by given ID
        Category category = repository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with given ID"));

        //update category
        category.setDescription(categoryDto.getDescription());
        category.setTitle(categoryDto.getTitle());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = repository.save(category);
        return mapper.map(updatedCategory,CategoryDto.class);

    }

    @Override
    public CategoryDto get(String categoryId) {
        Category category = repository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with given ID"));
        return mapper.map(category,CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {

        Category category = repository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category with given Id not found"));
        repository.delete(category);
        logger.info("Category with String Id"+categoryId +"deleted");
        logger.info(String.valueOf(category));
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()): (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page = repository.findAll(pageable);

        PageableResponse<CategoryDto>pageableResponse = Helper.getPageableResponse(page,CategoryDto.class);
        return pageableResponse;
    }
}
