package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class FriendshipId implements Serializable {

    private Long requester;
    private Long friend;

    public Long getRequester() {
        return requester;
    }

    public void setRequester(Long requester) {
        this.requester = requester;
    }

    public Long getFriend() {
        return friend;
    }

    public void setFriend(Long friend) {
        this.friend = friend;
    }

    public FriendshipId(Long requester, Long friend) {
        this.requester = requester;
        this.friend = friend;
    }

    public FriendshipId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FriendshipId friendshipId = (FriendshipId) o;
        return friend.equals(friendshipId.friend) && requester.equals(friendshipId.requester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friend, requester);
    }
}
