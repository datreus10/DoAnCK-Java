package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FriendShip")
public class FriendShip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long friendShipId;

    @JoinColumn(name = "fs_fromuser", referencedColumnName = "Id", nullable = false, updatable = false)
    @OneToOne(optional = false, targetEntity = User.class)
    public User fromUserId;

    @JoinColumn(name = "fs_touser", referencedColumnName = "Id", nullable = false, updatable = false)
    @OneToOne(optional = false, targetEntity = User.class)
    public User toUserId;

    public Long getFriendShipId() {
        return friendShipId;
    }

    public void setFriendShipId(Long friendShipId) {
        this.friendShipId = friendShipId;
    }

    public User getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(User fromUserId) {
        this.fromUserId = fromUserId;
    }

    public User getToUserId() {
        return toUserId;
    }

    public void setToUserId(User toUserId) {
        this.toUserId = toUserId;
    }

    public FriendShip() {
    }

    
}
