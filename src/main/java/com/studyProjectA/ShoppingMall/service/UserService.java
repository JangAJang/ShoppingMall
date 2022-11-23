package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.LoginRequestDto;
import com.studyProjectA.ShoppingMall.dto.RegisterDto;
import com.studyProjectA.ShoppingMall.dto.UserDto;
import com.studyProjectA.ShoppingMall.entity.Cart;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.EmailAlreadyExistsException;
import com.studyProjectA.ShoppingMall.excpetion.PasswordNotEqualException;
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
        User newUser = new User();
        userRepository.save(newUser.makeNewUser(registerDto));
        createCartForRegister(newUser);
        return UserDto.toDto(newUser);
    }

    @Transactional
    public UserDto login(LoginRequestDto loginRequestDto){
        User user = findUserByUsername(loginRequestDto.getUsername());
        if(user.getPassword().equals(bCryptPasswordEncoder.encode(loginRequestDto.getPassword()))){
            throw new PasswordNotEqualException();
        }
        return UserDto.toDto(user);
    }

    @Transactional(readOnly = true)
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
        validatePassword(registerDto);
    }

    public void validatePassword(RegisterDto registerDto){
        if(!registerDto.getPassword().equals(registerDto.getPasswordCheck())) throw new PasswordNotEqualException();
    }

    public void createCartForRegister(User user){
        Cart cart = Cart.builder()
                .user(user).build();
        cartRepository.save(cart);
    }


}