package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "Friendship")
public class Friendship {

    @EmbeddedId
    private FriendshipId friendshipId = new FriendshipId();

    @ManyToOne
    @MapsId("request")
    @JoinColumn(name = "request_id")
    private User request;

    @ManyToOne
    @MapsId("receive")
    @JoinColumn(name = "receive_id")
    private User receive;

    @Column(name = "TimeCreated", nullable = false)
    private LocalDateTime time;

    @Column(name = "Active", nullable = false)
    private String status;

    public FriendshipId getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(FriendshipId friendshipId) {
        this.friendshipId = friendshipId;
    }

    public User getRequest() {
        return request;
    }

    public void setRequest(User request) {
        this.request = request;
    }

    public User getReceive() {
        return receive;
    }

    public void setReceive(User receive) {
        this.receive = receive;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Friendship(User request, User receive, String status) {
        this.request = request;
        this.receive = receive;
        this.status = status;
        this.time = LocalDateTime.now();
    }

    public Friendship() {
    }

}
