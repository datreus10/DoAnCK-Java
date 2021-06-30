package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.example.demo.model.Friend;
import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repo.FriendRepo;
import com.example.demo.repo.NotificationRepo;
import com.example.demo.repo.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {

    @Autowired
    private UserService userService;

    @Autowired
    FriendRepo friendRepo;

    @Autowired
    AzureBlobService storageService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    private NotificationService notificationService;

    // 1 là thằng nhận
    // 2 la thằng gửi

    public void addFriend(Long userId1, Long userId2) throws NullPointerException {
        Optional<User> user1 = userRepo.findById(userId1);
        Optional<User> user2 = userRepo.findById(userId2);
        if (user1.isPresent() && user2.isPresent()) {
            User firstuser = user1.get();
            User seconduser = user2.get();
            if (!(friendRepo.existsByFirstUserAndSecondUser(firstuser, seconduser))) {
                friendRepo.save(new Friend(firstuser, seconduser));
                String nof = "Bạn đã nhận được lời mời kết bạn từ "+ seconduser.getFullName();
                notificationService.saveNotification(seconduser, firstuser, nof);
            }
        }
    }

    public String getFriendShip(Long userId1, Long userId2) {
        Optional<User> user1 = userRepo.findById(userId1);
        Optional<User> user2 = userRepo.findById(userId2);
        if (user1.isPresent() && user2.isPresent()) {
            User firstuser = user1.get();
            User seconduser = user2.get();
            if (friendRepo.existsByFirstUserAndSecondUser(firstuser, seconduser)) {
                Friend f = friendRepo.findByFirstUserAndSecondUserAndStatus(firstuser, seconduser, "accept");
                if (f != null)
                    return "friend"; // đã là bạn
                return "gui"; // nguoi gui
            }
            if (friendRepo.existsByFirstUserAndSecondUser(seconduser, firstuser)) {
                Friend f = friendRepo.findByFirstUserAndSecondUserAndStatus(seconduser, firstuser, "accept");
                if (f != null)
                    return "friend"; // đã là bạn
                return "nhan"; // nguoi nhan
            }
        }
        return null; // người xa lạ
    }

    public List<User> getFriendReceive() { // các lời mời kết bạn mà mình nhận
        Optional<User> user = userRepo.findById(userService.getCurrentUser().getUserId());
        List<User> friendUsers = new ArrayList<>();
        if (user.isPresent()) {
            List<Friend> lf = friendRepo.findByFirstUserAndStatus(user.get(), "wait");
            for (Friend f : lf) {
                User u = f.getSecondUser();
                u.setAvatarLink(storageService.getFileLink(u.getAvatar()));
                friendUsers.add(u);
            }
        }
        return friendUsers;
    }

    public List<User> getFriendRequest() { // các yêu cầu mà mình gửi kết bạn
        Optional<User> user = userRepo.findById(userService.getCurrentUser().getUserId());
        List<User> friendUsers = new ArrayList<>();
        if (user.isPresent()) {
            List<Friend> lf = friendRepo.findBySecondUserAndStatus(user.get(), "wait");
            for (Friend f : lf) {
                User u = f.getFirstUser();
                u.setAvatarLink(storageService.getFileLink(u.getAvatar()));
                friendUsers.add(u);
            }
        }
        return friendUsers;
    }

    @Transactional
    public void acceptFriend(Long userId1, Long userId2) throws NullPointerException {
        Optional<User> user1 = userRepo.findById(userId1); // nhận
        Optional<User> user2 = userRepo.findById(userId2); // gửi
        if (user1.isPresent() && user2.isPresent()) {
            User firstuser = user1.get();
            User seconduser = user2.get();
            if (friendRepo.existsByFirstUserAndSecondUser(firstuser, seconduser)) {
                Friend f = friendRepo.findByFirstUserAndSecondUser(firstuser, seconduser);
                notificationService.deleteNotification(seconduser, firstuser);
                f.setStatus("accept");
                String noti = firstuser.getFullName()+ " đã chấp nhận lời mời kết bạn";
                notificationService.saveNotification(firstuser, seconduser, noti);
            }
        }
    }

    public void denyFriend(Long userId1, Long userId2) throws NullPointerException {
        Optional<User> user1 = userRepo.findById(userId1); // nhận
        Optional<User> user2 = userRepo.findById(userId2); // gửi
        if (user1.isPresent() && user2.isPresent()) {
            User firstuser = user1.get();
            User seconduser = user2.get();
            if (friendRepo.existsByFirstUserAndSecondUser(firstuser, seconduser)) {
                Friend f = friendRepo.findByFirstUserAndSecondUserAndStatus(firstuser, seconduser, "wait");
                friendRepo.delete(f);
            }
        }
    }

    public List<User> getListFriend() {
        List<Friend> l1 = friendRepo.findByFirstUserAndStatus(userService.getCurrentUser(), "accept");
        List<Friend> l2 = friendRepo.findBySecondUserAndStatus(userService.getCurrentUser(), "accept");
        List<User> result = new ArrayList<>();

        for (Friend f : l1) {
            User u = f.getSecondUser();
            u.setAvatarLink(storageService.getFileLink(u.getAvatar()));
            result.add(u);
        }
        for (Friend f : l2) {
            User u = f.getFirstUser();
            u.setAvatarLink(storageService.getFileLink(u.getAvatar()));
            result.add(u);
        }
        return result;
    }

    public List<User> getListFriendByUser(User user) {
        List<Friend> l1 = friendRepo.findByFirstUserAndStatus(user, "accept");
        List<Friend> l2 = friendRepo.findBySecondUserAndStatus(user, "accept");
        List<User> result = new ArrayList<>();

        for (Friend f : l1) {
            User u = f.getSecondUser();
            u.setAvatarLink(storageService.getFileLink(u.getAvatar()));
            result.add(u);
        }
        for (Friend f : l2) {
            User u = f.getFirstUser();
            u.setAvatarLink(storageService.getFileLink(u.getAvatar()));
            result.add(u);
        }
        return result;
    }

    public void deleteFriend(Long userId) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()) {
            Friend f1 = friendRepo.findByFirstUserAndSecondUserAndStatus(userService.getCurrentUser(), user.get(),
                    "accept");
            Friend f2 = friendRepo.findByFirstUserAndSecondUserAndStatus(user.get(), userService.getCurrentUser(),
                    "accept");
            if (f1 != null)
                friendRepo.delete(f1);
            if (f2 != null)
                friendRepo.delete(f2);
        }
    }

    public boolean isFriend(User user) {
        if (friendRepo.findByFirstUserAndSecondUserAndStatus(userService.getCurrentUser(), user, "accept") != null)
            return true;
        if (friendRepo.findByFirstUserAndSecondUserAndStatus(user, userService.getCurrentUser(), "accept") != null)
            return true;
        return false;
    }
}