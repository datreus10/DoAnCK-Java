package com.example.demo.controller;

import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping()
    public String getMainPage(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("listPost", postService.getAllPost());
        return "index";
    }
}
