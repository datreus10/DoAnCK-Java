package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repo.NotificationRepo;
import com.example.demo.repo.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    public List<Notification> getNotificationsCurrentUser() {
        return notificationRepo.findByToUser(userService.getCurrentUser());
    }

    public void saveNotification(User fUser,User tUser,String content){
        User u = tUser;
        u.setSeenNotification(false);
        userRepo.save(u);
        notificationRepo.save(new Notification(content, fUser, tUser));
    }


    public void deleteNotification(User fUser,User tUser){
        List<Notification> n = notificationRepo.findByFromUserAndToUser(fUser, tUser);
        notificationRepo.delete(n.get(0));
    }
    
}
