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
    @JoinColumn(name = "from_user_id", referencedColumnName = "id")
    User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id", referencedColumnName = "id")
    User toUser;

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

    
    
    public Notification() {
    }

    public Notification(String content, User fUser, User tUser) {
        this.content = content;
        this.fromUser = fUser;
        this.toUser = tUser;
    }

    

    
    
    
}
