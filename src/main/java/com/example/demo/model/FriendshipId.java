package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class FriendshipId implements Serializable {

    private Long request;
    private Long receive;

    

    public Long getRequest() {
        return request;
    }

    public void setRequest(Long request) {
        this.request = request;
    }

    public Long getReceive() {
        return receive;
    }

    public void setReceive(Long receive) {
        this.receive = receive;
    }

    

    public FriendshipId(Long request, Long receive) {
        this.request = request;
        this.receive = receive;
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
        return request.equals(friendshipId.request) && receive.equals(friendshipId.receive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request,receive);
    }
}
