package com.studyProjectA.ShoppingMall.auth;


import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFound;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import com.studyProjectA.ShoppingMall.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return new PrincipalDetails(user);
    }
}
