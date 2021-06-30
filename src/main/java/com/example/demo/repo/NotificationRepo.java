package com.example.demo.repo;

import java.util.List;

import com.example.demo.model.Notification;
import com.example.demo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

    List<Notification>  findByToUser(User user);

    List<Notification> findByFromUserAndToUser(User fUser,User tUser);
}
