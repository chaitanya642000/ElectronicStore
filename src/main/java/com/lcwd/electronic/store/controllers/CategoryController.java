package com.lcwd.electronic.store.controllers;


import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    //create
    @PostMapping("/category")
    public ResponseEntity<ApiMessageResponse>createCategory(@RequestBody @Valid CategoryDto categoryDto)
    {
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        ApiMessageResponse response= ApiMessageResponse.builder().
                message("Category Created Successfully").status(HttpStatus.CREATED).success(true).build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto>updateCategoryById(@PathVariable("categoryId") String categoryId,
                                                 @Valid @RequestBody CategoryDto dto)
    {
        CategoryDto categoryDto1 = categoryService.update(dto,categoryId);
        return new ResponseEntity<>(categoryDto1,HttpStatus.ACCEPTED);
    }


    //delete
    @DeleteMapping("/{categoryId}")
    public ApiMessageResponse deleteCategoryById(@PathVariable("categoryId") String categoryId)
    {
        categoryService.delete(categoryId);
        ApiMessageResponse response = new ApiMessageResponse().builder()
                                      .message("Category with ID "+categoryId+" has been deleted")
                                       .success(true)
                                      .status(HttpStatus.OK)
                                       .build();

        ApiMessageResponse apiMessageResponse =  ApiMessageResponse.builder()
                            .message("Category deleted successfully")
                            .success(true)
                             .status(HttpStatus.OK).build();
        return response;
    }

    //get single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto>getCategory(@PathVariable(name = "categoryId") String
                                                  categoryId)
    {
        CategoryDto categorydto = categoryService.get(categoryId);
        return new ResponseEntity<>(categorydto,HttpStatus.OK);
    }

    //get All

    @GetMapping("/getAllCategories")
     public ResponseEntity<PageableResponse<CategoryDto>> getCategory(@RequestParam(defaultValue = "0",name = "pageNumber") int pageNumber,
                                                                      @RequestParam(defaultValue = "10",name="pageSize") int pageSize,
                                                                      @RequestParam(name = "sortBy",defaultValue = "title") String sortBy,
                                                                      @RequestParam(name = "sortDir",defaultValue = "ASC") String sortDir)
     {
             PageableResponse<CategoryDto>categories = categoryService.getAll(pageNumber,pageSize,sortBy,sortDir);
             return new ResponseEntity<>(categories,HttpStatus.OK);
     }

     @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto>createProductWithCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestBody ProductDto dto
     )
     {
        ProductDto productDto =  productService.createWithCategory(dto,categoryId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
     }

     //update category of the product
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto>updateProductWithCategory(@PathVariable
                                                               String categoryId,
                                                               @PathVariable
                                                               String productId)
    {
        ProductDto productDto = productService.updateCategory(categoryId,productId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

}
