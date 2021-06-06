package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    // render main page
    @GetMapping()
    public String getMainPage(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
       // model.addAttribute("recommedUsers",userService.getRecommendUsers());
        model.addAttribute("listPost", postService.getAllPost());
        return "index";
    }

    // render profile
    @GetMapping("/profile")
    public String getProfile(@RequestParam(name = "id") Long userId,
            @RequestParam(name = "postId", required = false) Long postId, Model model) {

        User guestUser = userService.getUserById(userId);
        User currentUser = userService.getCurrentUser();
        if (guestUser.equals(currentUser)) {
            model.addAttribute("isCurrentUser", true);
        } else {
            model.addAttribute("isCurrentUser", false);
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("guestUser", guestUser);
        if (postId != null) {
            model.addAttribute("listPost", postService.getPostByUserAndPostId(guestUser, postId));
        } else {
            model.addAttribute("listPost", postService.getPostByUser(guestUser));
        }

        return "profile";
    }

    // render setting account
    @GetMapping("/setting")
    public String getPage(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "settings";
    }
}
