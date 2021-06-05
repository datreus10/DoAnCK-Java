package com.example.demo.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long userId;

    @Column(name = "FirstName", nullable = false, columnDefinition = "nvarchar(50)")
    private String firstName;

    @Column(name = "LastName", nullable = false, columnDefinition = "nvarchar(50)")
    private String lastName;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "BirthDate")
    private LocalDate birthDate;

    @Column(name = "Avatar")
    private String avatar = "default_user_avatart.jpg";

    @Column(name = "Enable")
    private boolean enable;

    @Column(name = "VerificationCode", nullable = true)
    private String verificationCode;

    @Column(name = "AuthProvider")
    private String authProvider;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Post> posts;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "requester")
    private List<Friendship> friendRequests;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "friend")
    private List<Friendship> friends;


    

    

    public List<Friendship> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<Friendship> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public List<Friendship> getFriends() {
        return friends;
    }

    public void setFriends(List<Friendship> friends) {
        this.friends = friends;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(String authProvider) {
        this.authProvider = authProvider;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Transient
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public User() {
    }

}
