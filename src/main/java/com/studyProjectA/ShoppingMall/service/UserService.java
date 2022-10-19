package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.RegisterDto;
import com.studyProjectA.ShoppingMall.dto.UserDto;
import com.studyProjectA.ShoppingMall.entity.Cart;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.EmailAlreadyExistsException;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFoundException;
import com.studyProjectA.ShoppingMall.excpetion.UsernameAlreadyExistsException;
import com.studyProjectA.ShoppingMall.repository.CartRepository;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    //
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CartRepository cartRepository;

    @Transactional
    public UserDto register(RegisterDto registerDto){
        validateRegisterDto(registerDto);
        return UserDto.toDto(createUser(registerDto));
    }

    @Transactional
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User findUserByUsername(String username){
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return user;
    }

    public void validateUsername(String username){
        for(User user : findAllUsers()){
            if(user.getUsername().equals(username)) throw new UsernameAlreadyExistsException();
        }
    }

    public void validateEmail(String email){
        for(User user : findAllUsers()){
            if(user.getEmail().equals(email)) throw new EmailAlreadyExistsException();
        }
    }

    public void validateRegisterDto(RegisterDto registerDto){
        validateUsername(registerDto.getUsername());
        validateEmail(registerDto.getEmail());
    }

    public User createUser(RegisterDto registerDto){
        User user = addUserByRegisterDto(registerDto);
        createCartForRegister(user);
        return user;
    }

    public User addUserByRegisterDto(RegisterDto registerDto){
        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .address(registerDto.getAddress())
                .password(bCryptPasswordEncoder.encode(registerDto.getPassword()))
                .role("ROLE_USER").build();
        userRepository.save(user);
        return user;
    }

    public void createCartForRegister(User user){
        Cart cart = Cart.builder()
                .buyer(user).build();
        cartRepository.save(cart);
    }
}