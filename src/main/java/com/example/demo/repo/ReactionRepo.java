package com.example.demo.repo;


import com.example.demo.model.Post;
import com.example.demo.model.Reaction;
import com.example.demo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepo extends JpaRepository<Reaction, Long> {
   Reaction findByPostAndUser(Post post, User user);
}
