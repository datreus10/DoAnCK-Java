package com.example.demo.repo;

import java.util.List;

import com.example.demo.model.Friend;
import com.example.demo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepo extends JpaRepository<Friend, Integer> {

    boolean existsByFirstUserAndSecondUser(User first, User second);

    
    List<Friend> findByFirstUserAndStatus(User user, String status);
 
    List<Friend> findBySecondUserAndStatus(User user, String status);

    Friend findByFirstUserAndSecondUser(User first, User second);

    Friend findByFirstUserAndSecondUserAndStatus(User first, User second, String status);
}
