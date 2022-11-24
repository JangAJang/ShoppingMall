package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.ProductDto;
import com.studyProjectA.ShoppingMall.dto.ProductResponseDto;
import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.ProductNameAlreadyExistsException;
import com.studyProjectA.ShoppingMall.excpetion.ProductNotFoundException;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFoundException;
import com.studyProjectA.ShoppingMall.excpetion.UserSellerNotEqualException;
import com.studyProjectA.ShoppingMall.repository.ProductRepository;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 전체 물품 조회
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProducts(){
        return changeEntityToDto(getAllProducts());
    }

    // 단건 물품 조회
    @Transactional(readOnly = true)
    public ProductResponseDto searchProduct(long id){
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return ProductResponseDto.toDto(product);
    }

    //품목 검색
    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchProductsByName(String searchProductName){
        return changeEntityToDto(getProductsByName(searchProductName));
    }

    //검색한 사용자 품목 조회
    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchProductsByUsername(String username){
        User user = getUser(username);
        List<Product> products = getProductByUser(user);
        return changeEntityToDto(products);
    }

    // 아이템 등록
    @Transactional
    public ProductResponseDto addProduct(User loginUser, ProductDto productDto){
        Product product = new Product().makeProduct(productDto, loginUser);
        productRepository.save(product);
        return ProductResponseDto.toDto(product);
    }

    // 아이템 수정
    @Transactional
    public ProductResponseDto updateProduct(User loginUser, Long itemId, ProductDto productDto) {
        Product product = getProduct(itemId);
        validateUserAuthority(loginUser, product);
        product.makeProduct(productDto, loginUser);
        return ProductResponseDto.toDto(product);
    }


    // 아이템 삭제
    @Transactional
    public String deleteProduct(User loginUser, Long itemId) {
        Product product = getProduct(itemId);
        validateUserAuthority(loginUser, product);
        productRepository.delete(product);
        return "삭제 완료";
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getMyProducts(User loginUser){
        List<Product> products = getProductByUser(loginUser);
        return changeEntityToDto(products);
    }

    public List<Product> getAllProducts(){
        List<Product> products = productRepository.findAll();
        validateProductExistence(products);
        return products;
    }

    public List<ProductResponseDto> changeEntityToDto(List<Product> products){
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        products.forEach(s-> productResponseDtos.add(ProductResponseDto.toDto(s)));
        return productResponseDtos;
    }

    public void validateProductExistence(List<Product> products){
        if(products.isEmpty()) throw new ProductNotFoundException();
    }

    public void validateUserAuthority(User loginUser, Product product){
        if(!loginUser.equals(product.getUser())) throw new UserSellerNotEqualException();
    }

    public void validateProductNameExistence(String productName){
        for(Product product : getAllProducts()){
            if(product.getProductName().equals(productName)) throw new ProductNameAlreadyExistsException();
        }
    }

    public List<Product> getProductsByName(String name){
        List<Product> products = productRepository.findAllByProductNameContaining(name);
        validateProductExistence(products);
        return products;
    }

    public List<Product> getProductByUser(User user){
        List<Product> products = productRepository.findAllByUser(user);
        validateProductExistence(products);
        return products;
    }

    public User getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public Product getProduct(Long id){
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }
}