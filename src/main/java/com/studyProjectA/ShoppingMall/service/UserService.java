package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.RegisterDto;
import com.studyProjectA.ShoppingMall.dto.UserDto;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFound;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public UserDto register(RegisterDto registerDto){
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setAddress(registerDto.getAddress());
        user.setRole("ROLE_USER");
        System.out.println(user.getUsername());
        userRepository.save(user);
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

    public User findUser(int id){
        User user = userRepository.findById(id).orElseThrow(UserNotFound::new);
        return user;
    }

    public User findUserByUsername(String username){
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        return user;
    }
}
