package com.studyProjectA.ShoppingMall.oauth.provider;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getUsername();
}
