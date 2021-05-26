package com.example.demo.component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.model.CustomOAuth2User;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = userService.getUserByEmail(oAuth2User.getEmail());
        if (user == null) {
            userService.saveUserOAuth(oAuth2User.getEmail(), oAuth2User.getFullName(), oAuth2User.getOauthName());
        } else {
            userService.updateUserAfterOauthLogin(user, oAuth2User.getFullName(), oAuth2User.getOauthName());
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
