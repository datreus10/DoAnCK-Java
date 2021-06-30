package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long nId;

    @Column(columnDefinition = "nvarchar(MAX)")
    private String content;

    @ManyToOne
    @JoinColumn(name = "fUser")  
    private User fUser;


    @ManyToOne
    @JoinColumn(name = "tUser")  
    private User tUser;

    public Long getnId() {
        return nId;
    }

    public void setnId(Long nId) {
        this.nId = nId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getfUser() {
        return fUser;
    }

    public void setfUser(User fUser) {
        this.fUser = fUser;
    }

    public User gettUser() {
        return tUser;
    }

    public void settUser(User tUser) {
        this.tUser = tUser;
    }

    public Notification(String content, User fUser, User tUser) {
        this.content = content;
        this.fUser = fUser;
        this.tUser = tUser;
    }

    

    
    
    
}
