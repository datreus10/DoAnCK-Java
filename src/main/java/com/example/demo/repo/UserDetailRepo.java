package com.example.demo.repo;

import com.example.demo.model.User;
import com.example.demo.model.UserDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepo extends JpaRepository<UserDetail, Long> {
    
    UserDetail findByUser(User user);
}
