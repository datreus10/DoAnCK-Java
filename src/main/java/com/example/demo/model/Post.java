package com.example.demo.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @Column(name = "PostMedia", nullable = true)
    private String media;

    @Column
    private String mode = "public";

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private List<Reaction> reactions;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm | EEEE, d MMMM, y",new Locale("vi", "VN"));
        return postTime.format(formatter);
    }

    public LocalDateTime getPostTime2() {
        return this.postTime;
    }

    // public void setPostTime(LocalDateTime postTime) {
    // this.postTime = postTime;
    // }

    public User getUser() {
        return user;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPostTime(LocalDateTime postTime) {
        this.postTime = postTime;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public Post() {
        this.postTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

}
