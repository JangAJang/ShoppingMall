package com.studyProjectA.ShoppingMall.UserService;

import com.studyProjectA.ShoppingMall.Dto.RegisterDto;
import com.studyProjectA.ShoppingMall.Entity.User;
import com.studyProjectA.ShoppingMall.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User register(RegisterDto registerDto){
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setEmail(registerDto.getEmail());
        user.setAddress(registerDto.getAddress());
        user.setRole("USER");
        return userRepository.save(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findUser(int id){
        return userRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("해당 ID를 찾을 수 없습니다. ");
        });
    }
}
