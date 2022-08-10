package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.CartDto;
import com.studyProjectA.ShoppingMall.dto.UserDto;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.repository.CartRepository;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Transactional(readOnly = true)
    public CartDto showMyCart(UserDto userDto){
        User user = userRepository.findById(userDto.getId()).orElseThrow(()->{
            return new IllegalArgumentException("유저를 찾을 수 없습니다. ");
        });
        return CartDto.toDto(cartRepository.findByUser(user.getUsername()));
    }


}
