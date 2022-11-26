package com.studyProjectA.ShoppingMall.oauth;

import com.studyProjectA.ShoppingMall.auth.PrincipalDetails;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFoundException;
import com.studyProjectA.ShoppingMall.oauth.provider.*;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = initializeUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = createInfoByProvider(oAuth2User, userRequest);
        User userEntity = userRepository.findByUsername(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                .orElseThrow(UserNotFoundException::new);
        if(isNotExistingUser(userEntity)){ //아이디 없음. 회원가입 진행
            userEntity = createUser(oAuth2UserInfo);
            userRepository.save(userEntity);
        }
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }

    private boolean isNotExistingUser(User user){
        return user == null;
    }

    private User createUser(OAuth2UserInfo oAuth2UserInfo){
        return User.builder()
                .username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                .password(bCryptPasswordEncoder.encode("겟인데어"))
                .email(oAuth2UserInfo.getEmail())
                .role("ROLE_USER")
                .address("주소 변경 필요")
                .providerId(oAuth2UserInfo.getProviderId())
                .provider(oAuth2UserInfo.getProvider())
                .build();
    }

    private OAuth2User initializeUser(OAuth2UserRequest userRequest){
        System.out.println("UserRequest : " + userRequest.getClientRegistration());
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes : " + oAuth2User.getAttributes());
        return oAuth2User;
    }

    private OAuth2UserInfo createInfoByProvider(OAuth2User oAuth2User, OAuth2UserRequest userRequest){
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            return  new GoogleUserInfo(oAuth2User.getAttributes());
        }
        if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            return new FacebookUserInfo(oAuth2User.getAttributes());
        }
        if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            return  new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        }
        if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")){
            return new KakaoUserInfo((Map)oAuth2User.getAttributes().get("profile"));
        }
        return null;
    }
}