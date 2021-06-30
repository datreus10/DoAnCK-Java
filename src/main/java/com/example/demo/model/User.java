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
import javax.persistence.OneToOne;
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

    @Column(name = "BackgroundImage")
    private String bgImg = "default_user_bg.jpg";

    @Column(name = "Enable")
    private boolean enable;

    @Column(name = "VerificationCode", nullable = true)
    private String verificationCode;

    @Column(name = "AuthProvider")
    private String authProvider;

    @Column(name = "ResetPasswordToken")
    private String resetPasswordToken;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Post> posts;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fUser")
    private List<Notification> fNotifications;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tUser")
    private List<Notification> tNotifications;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "firstUser")
    private List<Friend> friendRequest;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "secondUser")
    private List<Friend> friendReceive;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private UserDetail userDetail;

    @Transient
    private String avatarLink;

    @Transient
    private String bgLink;

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
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

    public List<Friend> getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(List<Friend> friendRequest) {
        this.friendRequest = friendRequest;
    }

    public List<Friend> getFriendReceive() {
        return friendReceive;
    }

    public void setFriendReceive(List<Friend> friendReceive) {
        this.friendReceive = friendReceive;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }

    public String getBgLink() {
        return bgLink;
    }

    public void setBgLink(String bgLink) {
        this.bgLink = bgLink;
    }

    @Transient
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }


    public List<Notification> getfNotifications() {
        return fNotifications;
    }

    public void setfNotifications(List<Notification> fNotifications) {
        this.fNotifications = fNotifications;
    }


    

    public List<Notification> gettNotifications() {
        return tNotifications;
    }

    public void settNotifications(List<Notification> tNotifications) {
        this.tNotifications = tNotifications;
    }

    public User() {
    }

}
