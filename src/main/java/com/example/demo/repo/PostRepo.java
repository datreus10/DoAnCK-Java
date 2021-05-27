package com.example.demo.repo;

import java.util.List;

import com.example.demo.model.Post;
import com.example.demo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long>{
    List<Post> findPostByUserOrderByPostTimeDesc(User user);

    List<Post> findAllByOrderByPostTimeDesc();
}
