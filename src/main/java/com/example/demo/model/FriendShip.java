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
@Table(name = "FriendShip")
public class Friendship {

    @EmbeddedId
    private FriendshipId friendshipId = new FriendshipId();

    
    @ManyToOne
    @MapsId("requester")
    @JoinColumn(name = "requester_id")
    private User requester;


    @ManyToOne
    @MapsId("friend")
    @JoinColumn(name = "friend_id")
    private User friend;

    @Column(name = "TimeCreated", nullable = false)
    private LocalDateTime time;

    @Column(name = "Active", nullable = false)
    private String status;

    public User getRequester() {
        return requester;
    }

    public FriendshipId getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(FriendshipId friendshipId) {
        this.friendshipId = friendshipId;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
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

    public Friendship() {
    }

    public Friendship(User requester, User friend, String status) {
        this.requester = requester;
        this.friend = friend;
        this.status = status;
        this.time = LocalDateTime.now();
    }

}
