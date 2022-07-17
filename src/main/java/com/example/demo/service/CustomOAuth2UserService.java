package com.example.demo.service;

import com.example.demo.model.CustomOAuth2User;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService  {
 
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user =  super.loadUser(userRequest);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(user);
        String oauthName = userRequest.getClientRegistration().getClientName();
        customOAuth2User.setOauthName(oauthName);
        return customOAuth2User;
    }
 
}