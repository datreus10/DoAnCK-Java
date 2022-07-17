package com.example.demo.controller;

import java.util.Arrays;

import com.example.demo.model.User;
import com.example.demo.model.UserDetail;
import com.example.demo.service.FriendService;
import com.example.demo.service.NotificationService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;
import com.example.demo.service.storage.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class AppController {
    @Autowired
    private StorageService storageService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private NotificationService notificationService;

    // render main page
    @GetMapping()
    public String getMainPage(Model model) {
        model.addAttribute("onlineUsers", userService.getUsersFromSessionRegistry());
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("recommedUsers", userService.getRecommendUsers());
        model.addAttribute("listPost", postService.getPostMainPage());
        model.addAttribute("nof", notificationService.getNotificationsCurrentUser());
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
        model.addAttribute("nof", notificationService.getNotificationsCurrentUser());
        return "profile";
    }

    // render setting account
    @GetMapping("/setting")
    public String getPage(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("nof", notificationService.getNotificationsCurrentUser());
        return "settings";
    }

    // render setting account
    @GetMapping("/setting-about")
    public String getPageAboutSettings(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("nof", notificationService.getNotificationsCurrentUser());
        if (userService.getUserDetail() == null) {
            model.addAttribute("userDetail", new UserDetail());
        } else {
            model.addAttribute("userDetail", userService.getUserDetail());
        }
        return "settings-contact";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
