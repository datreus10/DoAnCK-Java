package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repo.NotificationRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private UserService userService;

    public List<Notification> getNotificationsCurrentUser() {
        return notificationRepo.findByToUser(userService.getCurrentUser());
    }

    public void saveNotification(User fUser,User tUser,String content){
        notificationRepo.save(new Notification(content, fUser, tUser));
    }

    // public List<Notification> getFriendNotification() {
    //     return notificationRepo.findByToUser(userService.getCurrentUser()).stream()
    //             .filter(e -> e.getContent().contains("kết bạn")).collect(Collectors.toList());
    // }

    public void deleteNotification(User fUser,User tUser){
        List<Notification> n = notificationRepo.findByFromUserAndToUser(fUser, tUser);
        notificationRepo.delete(n.get(0));
    }
    
}
