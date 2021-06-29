package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;

import com.example.demo.model.User;
import com.example.demo.model.UserDetail;
import com.example.demo.service.AzureBlobService;
import com.example.demo.service.FriendService;
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

    @Autowired
    private FriendService friendService;

    @Autowired
    private AzureBlobService storageService;

    // render main page
    @GetMapping()
    public String getMainPage(Model model) {
        // List<User> onlineUsers = userService.getUsersFromSessionRegistry();
        // onlineUsers.remove(userService.getCurrentUser());
        model.addAttribute("onlineUsers", userService.getUsersFromSessionRegistry());
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("recommedUsers", userService.getRecommendUsers());
        model.addAttribute("listPost", postService.getPostMainPage());
        return "index";
    }

    // render profile
    @GetMapping("/profile")
    public String getProfile(@RequestParam(name = "id") Long userId,
            @RequestParam(name = "postId", required = false) Long postId, Model model) {

        

        User guestUser = userService.getUserById(userId);
        User currentUser = userService.getCurrentUser();

        model.addAttribute("isCurrentUser", false);
        model.addAttribute("detailUser", userService.getUserDetailByUser(guestUser));
        if (guestUser.equals(currentUser)) {
            model.addAttribute("isCurrentUser", true);
            model.addAttribute("detailUser", userService.getUserDetail());
            model.addAttribute("listPost", postService.getPostByUser(currentUser));
        } else if (friendService.isFriend(guestUser)) {
            model.addAttribute("listPost",
                    postService.getPostByUserAndMode(Arrays.asList(guestUser), Arrays.asList("public", "friend")));
        } else {
            model.addAttribute("listPost",
                    postService.getPostByUserAndMode(Arrays.asList(guestUser), Arrays.asList("public")));
        }
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("guestUser", guestUser);
        model.addAttribute("friendList", friendService.getListFriendByUser(guestUser));
        model.addAttribute("friendShip", friendService.getFriendShip(userId, userService.getCurrentUser().getUserId()));
        return "profile";
    }

    // render setting account
    @GetMapping("/setting")
    public String getPage(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "settings";
    }

    // render setting account
    @GetMapping("/setting-about")
    public String getPageAboutSettings(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        if (userService.getUserDetail() == null) {
            model.addAttribute("userDetail", new UserDetail());
        } else {
            model.addAttribute("userDetail", userService.getUserDetail());
        }
        return "settings-contact";
    }
}
