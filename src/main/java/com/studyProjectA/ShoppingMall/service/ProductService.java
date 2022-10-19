package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.ProductResponseDto;
import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.ProductNotFoundException;
import com.studyProjectA.ShoppingMall.excpetion.UserNotEqualsException;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFoundException;
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
        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> productResponseDto = new ArrayList<>();
        products.forEach(s-> productResponseDto.add(ProductResponseDto.toDto(s)));
        return productResponseDto;
    }

    // 단건 물품 조회
    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(long id){
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        return ProductResponseDto.toDto(product);
    }

    //품목 검색
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getSearchProducts(String searchProductName){

        List<Product> products = productRepository.findAllByProductNameContaining(searchProductName);
        if (products.isEmpty())throw new ProductNotFoundException();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        products.forEach(s->productResponseDtos.add(ProductResponseDto.toDto(s)));
        return productResponseDtos;
    }

    //검색한 사용자 품목 조회
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getUserProducts(String userName){
        User user = userRepository.findByUsername(userName).orElseThrow(UserNotFoundException::new);
        List<Product> products = productRepository.findAllByUser(user);
        if (products.isEmpty())throw new ProductNotFoundException();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        products.forEach(s->productResponseDtos.add(ProductResponseDto.toDto(s)));
        if(productResponseDtos.isEmpty()) throw new ProductNotFoundException();
        return productResponseDtos;
    }

    // 아이템 등록
    @Transactional
    public ProductResponseDto addProduct(User loginUser, ProductResponseDto productResponseDto){
        Product product = Product.builder()
                .productName(productResponseDto.getProductName())
                .category(productResponseDto.getCategory())
                .deliveryDate(productResponseDto.getDeliveryDate())
                .price(productResponseDto.getPrice())
                .quantity(productResponseDto.getQuantity())
                .user(loginUser).build();
        System.out.println(product);
        productRepository.save(product);
        return ProductResponseDto.toDto(product);
    }

    // 아이템 수정
    @Transactional
    public ProductResponseDto updateProduct(User loginUser, Long itemId, ProductResponseDto productResponseDto) {
        Product product = productRepository.findById(itemId).orElseThrow(ProductNotFoundException::new);
        if(loginUser.equals(product.getUser())) {
            // 토큰 보낸 사람과, 게시글 작성자가 같다면 성공!
            product.setProductName(productResponseDto.getProductName());
            product.setPrice(productResponseDto.getPrice());
            product.setQuantity(productResponseDto.getQuantity());
            product.setCategory(productResponseDto.getCategory());
            product.setDeliveryDate(productResponseDto.getDeliveryDate());
            productRepository.save(product);
            return ProductResponseDto.toDto(product);
        } else {
            // 토큰 보낸 사람과 게시글 작성자가 다르다면 실패!
            throw new UserNotEqualsException();

        }
    }

    // 아이템 삭제
    @Transactional
    public String deleteProduct(User loginUser, Long itemId) {
        Product product = productRepository.findById(itemId).orElseThrow(ProductNotFoundException::new);
        if(loginUser.equals(product.getUser())) {
            productRepository.deleteById(itemId);
            return "삭제 완료";
        } else {
            throw new UserNotEqualsException();
        }
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getMyProducts(User loginUser){
        List<Product> products = productRepository.findAllByUser(loginUser);
        if (products.isEmpty())throw new ProductNotFoundException();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        products.forEach(s->productResponseDtos.add(ProductResponseDto.toDto(s)));
        if(productResponseDtos.isEmpty()) throw new ProductNotFoundException();
        return productResponseDtos;
    }
}