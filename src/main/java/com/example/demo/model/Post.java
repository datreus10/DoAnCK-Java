package com.example.demo.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long postId;

    @Column(name = "PostContent", nullable = false, columnDefinition = "nvarchar(MAX)")
    private String postContent;

    @Column(name = "PostTime", nullable = false)
    private LocalDateTime postTime;

    @ManyToOne
    @JoinColumn(name = "userId")  
    private User user;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm a");
        return postTime.format(formatter);
    }

    // public void setPostTime(LocalDateTime postTime) {
    //     this.postTime = postTime;
    // }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post() {
        this.postTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    
}
