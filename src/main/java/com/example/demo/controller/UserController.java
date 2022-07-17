package com.example.demo.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.demo.service.FriendService;
import com.example.demo.service.NotificationService;
import com.example.demo.service.UserService;
import com.example.demo.service.storage.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/update-avatar")
    public ResponseEntity<String> updateAvatar(@RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            String newAvatar = userService.updateAvatar(file);
            return new ResponseEntity<>(storageService.getFileLink(newAvatar), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while update avatar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-bg")
    public ResponseEntity<String> updateBackground(@RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            String newBg = userService.updateBackground(file);
            return new ResponseEntity<>(storageService.getFileLink(newBg), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while update avatar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-account")
    public String updateAccoun(@RequestParam Map<String, String> body) {
        userService.updateAccount(body.get("firstName"), body.get("lastName"), body.get("birthDate"));
        return "redirect:/setting";
    }

    @PostMapping("/update-account-detail")
    public String updateAccountDetail(@RequestParam Map<String, String> body) {
        userService.updateUserDetails(body);
        return "redirect:/setting-about";
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUser(@RequestParam(name = "keyword") String keyword) {
        try {
            return new ResponseEntity<>(userService.searchUsers(keyword), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/friend-check")
    public String friendCheck(Model model) {
        model.addAttribute("onlineUsers", userService.getUsersFromSessionRegistry());
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("receive", friendService.getFriendReceive());
        model.addAttribute("request", friendService.getFriendRequest());
        model.addAttribute("listFriend", friendService.getListFriend());
        model.addAttribute("recommedUsers",userService.getRecommendUsers());
        model.addAttribute("nof",notificationService.getNotificationsCurrentUser());
        return "friend-check";
    }

    @GetMapping("/add-friend") // tạo lời mời kết bạn
    public String addFriend(@RequestParam(name = "id") Long friendId,HttpServletRequest request) {
        friendService.addFriend(friendId, userService.getCurrentUser().getUserId());
        return "redirect:/user/friend-check";
    }

    @GetMapping("/accept-friend") // chấp nhận lời mời kết bạn
    public String acceptFriend(@RequestParam(name = "id") Long friendId) {
        friendService.acceptFriend(userService.getCurrentUser().getUserId(), friendId);
        return "redirect:/user/friend-check";
    }

    @GetMapping("/deny-friend") // từ chối lời mời kết bạn
    public String denyFriend(@RequestParam(name = "id") Long friendId) {
        friendService.denyFriend(userService.getCurrentUser().getUserId(), friendId);
        return "redirect:/user/friend-check";
    }

    @GetMapping("/decline-friend") // hủy yêu cầu kết bạn
    public String declineFriend(@RequestParam(name = "id") Long friendId) {
        friendService.denyFriend(friendId, userService.getCurrentUser().getUserId());
        return "redirect:/user/friend-check";
    }

    @GetMapping("/delete-friend") // hủy yêu cầu kết bạn
    public String deleteFriend(@RequestParam(name = "id") Long friendId) {
        friendService.deleteFriend(friendId);
        return "redirect:/user/friend-check";
    }

    @GetMapping("/seen-notification") 
    public void seenNotification() {
        userService.setSeenNotification(true);
    }

}
