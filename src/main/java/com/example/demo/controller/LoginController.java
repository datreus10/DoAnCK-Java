package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public ResponseEntity<String> loginError(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
                if (errorMessage.equals("Bad credentials")) {
                    errorMessage = "Mật khẩu không đúng";
                }
                
            }
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        //return new ResponseEntity<>("Đăng nhập thành công", HttpStatus.OK);
    }

    @GetMapping("/login-success")
    public ResponseEntity<String> loginSuccess(){
        return new ResponseEntity<>("Đăng nhập thành công", HttpStatus.OK);
    }

}
