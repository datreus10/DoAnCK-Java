package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.example.demo.model.Friend;
import com.example.demo.model.User;
import com.example.demo.repo.FriendRepo;
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

    public void addFriend(Long userId1, Long userId2) throws NullPointerException {
        Optional<User> user1 = userRepo.findById(userId1);
        Optional<User> user2 = userRepo.findById(userId2);
        if (user1.isPresent() && user2.isPresent()) {
            User firstuser = user1.get();
            User seconduser = user2.get();
            if (user1.get().getUserId() > user2.get().getUserId()) {
                firstuser = user2.get();
                seconduser = user1.get();
            }
            if (!(friendRepo.existsByFirstUserAndSecondUser(firstuser, seconduser))) {
                friendRepo.save(new Friend(firstuser, seconduser));
            }
        }
    }

    @Transactional
    public void acceptFriend(Long userId1, Long userId2) throws NullPointerException {
        Optional<User> user1 = userRepo.findById(userId1);
        Optional<User> user2 = userRepo.findById(userId2);
        if (user1.isPresent() && user2.isPresent()) {
            User firstuser = user1.get();
            User seconduser = user2.get();
            if (user1.get().getUserId() > user2.get().getUserId()) {
                firstuser = user2.get();
                seconduser = user1.get();
            }
            if (friendRepo.existsByFirstUserAndSecondUser(firstuser, seconduser)) {
                Friend f = friendRepo.findByFirstUserAndSecondUser(firstuser, seconduser);
                f.setStatus("accept");
            }
        }
    }

    public List<User> getFriendsRequest() {

        User currentUser = userService.getCurrentUser();
        List<Friend> friendsByFirstUser = friendRepo.findByFirstUserAndStatus(currentUser, "wait");
        List<Friend> friendsBySecondUser = friendRepo.findBySecondUserAndStatus(currentUser, "wait");
        List<User> friendUsers = new ArrayList<>();

        /*
         * suppose there are 3 users with id 1,2,3. if user1 add user2 as friend
         * database record will be first user = user1 second user = user2 if user3 add
         * user2 as friend database record will be first user = user2 second user =
         * user3 it is because of lexicographical order while calling get friends of
         * user 2 we need to check as a both first user and the second user
         */
        for (Friend friend : friendsByFirstUser) {
            User u = userRepo.findById(friend.getSecondUser().getUserId()).get();
            u.setAvatarLink(storageService.getFileLink(u.getAvatar()));
            friendUsers.add(u);
        }
        for (Friend friend : friendsBySecondUser) {
            User u = userRepo.findById(friend.getFirstUser().getUserId()).get();
            u.setAvatarLink(storageService.getFileLink(u.getAvatar()));
            friendUsers.add(userRepo.findById(friend.getFirstUser().getUserId()).get());
        }
        return friendUsers;

    }

    public Friend getFriendShip(Long userId1, Long userId2) {
        Optional<User> user1 = userRepo.findById(userId1);
        Optional<User> user2 = userRepo.findById(userId2);
        if (user1.isPresent() && user2.isPresent()) {
            User firstuser = user1.get();
            User seconduser = user2.get();
            if (user1.get().getUserId() > user2.get().getUserId()) {
                firstuser = user2.get();
                seconduser = user1.get();
            }
            if (friendRepo.existsByFirstUserAndSecondUser(firstuser, seconduser)) {
                return friendRepo.findByFirstUserAndSecondUser(firstuser, seconduser);
            }
        }
        return null;
    }



    public List<User> getListFriends() {

        User currentUser = userService.getCurrentUser();
        List<Friend> friendsByFirstUser = friendRepo.findByFirstUserAndStatus(currentUser, "accept");
        List<Friend> friendsBySecondUser = friendRepo.findBySecondUserAndStatus(currentUser, "accept");
        List<User> friendUsers = new ArrayList<>();

        /*
         * suppose there are 3 users with id 1,2,3. if user1 add user2 as friend
         * database record will be first user = user1 second user = user2 if user3 add
         * user2 as friend database record will be first user = user2 second user =
         * user3 it is because of lexicographical order while calling get friends of
         * user 2 we need to check as a both first user and the second user
         */
        for (Friend friend : friendsByFirstUser) {
            User u = userRepo.findById(friend.getSecondUser().getUserId()).get();
            u.setAvatarLink(storageService.getFileLink(u.getAvatar()));
            friendUsers.add(u);
        }
        for (Friend friend : friendsBySecondUser) {
            User u = userRepo.findById(friend.getFirstUser().getUserId()).get();
            u.setAvatarLink(storageService.getFileLink(u.getAvatar()));
            friendUsers.add(userRepo.findById(friend.getFirstUser().getUserId()).get());
        }
        return friendUsers;

    }
}