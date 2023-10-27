package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    //create
    ProductDto create(ProductDto productDto);

    //update
    ProductDto update(ProductDto productDto,String Id);

    //delete
    ProductDto deleteProduct(String Id);

    //get single
    ProductDto get(String Id);

    //get all
    PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

    //get all live
    PageableResponse<ProductDto>getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir);

    //search live
    PageableResponse<ProductDto>searchByTitle(String subTitleint,int pageNumber,int pageSize,String sortBy,String sortDir);

     ProductDto createWithCategory(ProductDto product,String category_id);

     ProductDto updateCategory(String productId,String categoryId);


}
