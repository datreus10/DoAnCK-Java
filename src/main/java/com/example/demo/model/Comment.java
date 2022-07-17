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
@Table(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(name = "CommentContent", nullable = false, columnDefinition = "nvarchar(MAX)")
    private String commentContent;

    @Column(name = "CommentTime", nullable = false)
    private LocalDateTime commentTime;

    @ManyToOne
    @JoinColumn(name = "postId"/* , referencedColumnName = "postId", nullable = false */)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "userId") 
    private User user;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return commentTime.format(formatter);
    }

    // public void setCommentTime(LocalDateTime commentTime) {
    //     this.commentTime = commentTime;
    // }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment() {
        this.commentTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

}
