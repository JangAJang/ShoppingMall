package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.RegisterDto;
import com.studyProjectA.ShoppingMall.dto.UserDto;
import com.studyProjectA.ShoppingMall.entity.Cart;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFoundException;
import com.studyProjectA.ShoppingMall.repository.CartRepository;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    //
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CartRepository cartRepository;

    public UserDto register(RegisterDto registerDto){
        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .address(registerDto.getAddress())
                .role("ROLE_USER").build();
        userRepository.save(user);
        Cart cart = Cart.builder()
                .buyer(user).build();
        cartRepository.save(cart);
        return UserDto.toDto(user);
    }

    public List<UserDto> findAll(){
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for(User user : users){
            userDtos.add(UserDto.toDto(user));
        }
        return userDtos;
    }

    public UserDto getMyPageInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        return UserDto.toDto(user);
    }

    public User findUserByUsername(String username){
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return user;
    }
}