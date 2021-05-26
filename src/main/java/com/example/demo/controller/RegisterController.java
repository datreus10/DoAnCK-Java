package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping()
    public ResponseEntity<String> reply(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        try {
            String siteURL = request.getRequestURL().toString();
            userService.registerUser(body, siteURL);
        } catch (IllegalStateException | MessagingException | UnsupportedEncodingException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Đăng kí thành công", HttpStatus.OK);
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verifyUser(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
}
