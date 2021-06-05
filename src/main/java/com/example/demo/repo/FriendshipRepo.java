package com.example.demo.repo;

import com.example.demo.model.Friendship;
import com.example.demo.model.FriendshipId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepo extends JpaRepository<Friendship, FriendshipId> {
    
}