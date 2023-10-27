package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.productimage.path}")
    private String fullFilePath;

    @Override
    public ProductDto create(ProductDto productDto) {

         Product product = mapper.map(productDto, Product.class);

        //add product Id
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);

        product.setAddedDate(new Date());

        Product savedProduct = productRepository.save(product);
        ProductDto productDto1 = mapper.map(savedProduct,ProductDto.class);
        return productDto1;
    }

    @Override
    public ProductDto update(ProductDto productDto, String Id) {
        Product product = productRepository.findById(Id).orElseThrow(()->new ResourceNotFoundException("" +
                "Product Not Found"));

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());

        Product updatedProduct = productRepository.save(product);
        return mapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public ProductDto deleteProduct(String Id) {
        Product product = productRepository.findById(Id).orElseThrow(()->new ResourceNotFoundException("" +
                "Product Not Found"));

        if(!product.getProductImageName().equals("defaultimage.jpg"))
        {
            String fullpath = fullFilePath+ File.separator+product.getProductImageName();
            Path path = Paths.get(fullpath);

            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }

        ProductDto productdeletedDto = entityToDto(product);
        productRepository.deleteById(Id);
        return productdeletedDto;

    }

    private ProductDto entityToDto(Product product) {

        ProductDto dto = mapper.map(product, ProductDto.class);
        return dto;
    }
    @Override
    public ProductDto get(String Id) {
       Product product =  productRepository.findById(Id).orElseThrow(() -> new ResourceNotFoundException("Product not " +
                "Find Exception"));
       return mapper.map(product,ProductDto.class);
    }


    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()): (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findAll(pageable);

        PageableResponse<ProductDto>pageableResponse = Helper.getPageableResponse(page,ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()): (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);

        PageableResponse<ProductDto>pageableResponse = Helper.getPageableResponse(page,ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()): (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByTitleContaining(subTitle,pageable);

        PageableResponse<ProductDto>pageableResponse = Helper.getPageableResponse(page,ProductDto.class);
        return pageableResponse;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String category_id) {
        Category category = categoryRepository.findById(category_id).orElseThrow(()->
           new ResourceNotFoundException("Category with given id not found")
        );

        Product product = mapper.map(productDto, Product.class);

        //add product Id
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        ProductDto productDto1 = mapper.map(savedProduct,ProductDto.class);

        return productDto1;
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product" +
                "with given Id doesnt exists"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category with given Id does not exists"));

        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);

    }
}
