package com.example.demo.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import com.example.demo.model.CustomOAuth2User;
import com.example.demo.model.User;
import com.example.demo.model.UserDetail;
import com.example.demo.repo.UserDetailRepo;
import com.example.demo.repo.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.bytebuddy.utility.RandomString;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AzureBlobService storageService;

    @Autowired
    private UserDetailRepo userDetailRepo;

    @Autowired
    private FriendService friendService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Tên đăng nhập không tồn tại");
        }
        if (!user.isEnable())
            throw new IllegalStateException("Email chưa được xác thực. </br> Hãy kiểm tra lại Email để kích hoạt");
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority("Role_User")));
    }

    public void registerUser(Map<String, Object> data, String siteURL)
            throws IllegalStateException, MessagingException, UnsupportedEncodingException {
        User user = new User();
        for (String key : data.keySet()) {
            String value = (String) data.get(key);
            if (value == null || value.isBlank()) {
                throw new IllegalStateException(key + " not null or empty");
            }
            if (key.equals("firstName")) {
                user.setFirstName(value);
            }
            if (key.equals("lastName")) {
                user.setLastName(value);
            }
            if (key.equals("email")) {
                user.setEmail(value);
            }
            if (key.equals("password_1")) {
                user.setPassword(passwordEncoder.encode(value));
            }
            if (key.equals("birthDate")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                user.setBirthDate(LocalDate.parse(value, formatter));
            }
        }
        if (userRepo.findByEmail(user.getEmail()) != null) {
            throw new IllegalStateException("Email đã được đăng kí.");
        }
        user.setVerificationCode(RandomString.make(64));
        sendVerificationCode(user, siteURL);
        userRepo.save(user);
    }

    private void sendVerificationCode(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "bankhongphailanguoimay@gmail.com";
        String senderName = "Mạng xã hội DM";
        String subject = "Xác thực tài khoản của bạn";
        String content = "Xin chào [[name]],<br>"
                + "Bạn hãy click vào link sau đây để hoàn tất việc đăng ký tài khoản:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">XÁC NHẬN</a></h3>" + "Cám ơn bạn,<br>" + "Mạng xã hội DM.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFullName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public String updateResetPasswordToken(String email) throws UsernameNotFoundException {
        String token = RandomString.make(30);
        User user = userRepo.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepo.save(user);
            return token;
        } else {
            throw new UsernameNotFoundException("Could not find any user with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepo.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepo.save(user);
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("bankhongphailanguoimay@gmail.com", "DM Social Network Support");
        helper.setTo(recipientEmail);

        String subject = "Đây là link để reset lại mật khẩu của bạn";

        String content = "<p>Xin chào,</p>" + "<p>Bạn đã yêu cầu rest lại mật khẩu.</p>"
                + "<p>Vui lòng click vào đường link dưới đây để đổi mật khẩu:</p>" + "<p><a href=\"" + link
                + "\">Thay đổi mật khẩu</a></p>" + "<br>" + "<p>Hãy bỏ qua email này nếu như bạn đã nhớ mật khẩu, "
                + "hoặc bạn không yêu cầu đổi mật khẩu.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public boolean verifyUser(String code) {
        User user = userRepo.findByVerificationCode(code);
        if (user == null || user.isEnable())
            return false;
        user.setVerificationCode(null);
        user.setEnable(true);
        UserDetail u = new UserDetail();
        user.setUserDetail(u);
        u.setUser(user);
        userRepo.save(user);
        return true;
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public void saveUserOAuth(String email, String fullName, String provider) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode("OAuth2"));
        String[] names = fullName.split("\\s+");
        if (names.length == 2) {
            newUser.setFirstName(names[0]);
            newUser.setLastName(names[1]);
        } else {
            newUser.setFirstName(fullName);
            newUser.setLastName("");
        }
        newUser.setEnable(true);
        newUser.setAuthProvider(provider);
        UserDetail u = new UserDetail();
        newUser.setUserDetail(u);
        u.setUser(newUser);
        userRepo.save(newUser);

    }

    public void updateUserAfterOauthLogin(User user, String fullName, String oauthName) {
        user.setFirstName(fullName);
        user.setLastName("");

        String[] names = fullName.split("\\s+");
        if (names.length == 2) {
            user.setFirstName(names[0]);
            user.setLastName(names[1]);
        } else {
            user.setFirstName(fullName);
            user.setLastName("");
        }

        user.setAuthProvider(oauthName);
        userRepo.save(user);
    }

    public User getCurrentUser() {
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomOAuth2User) {
            String email = ((CustomOAuth2User) principal).getEmail();
            user = userRepo.findByEmail(email);
        } else {
            String currentPrincipalName = authentication.getName();
            user = userRepo.findByEmail(currentPrincipalName);
        }
        user.setAvatarLink(storageService.getFileLink(user.getAvatar()));
        return user;
    }

    public UserDetail getUserDetail() {
        return userDetailRepo.findByUser(getCurrentUser());
    }

    public UserDetail getUserDetailByUser(User user) {
        return userDetailRepo.findByUser(user);
    }

    public User getUserById(Long id) {
        try {
            User user = userRepo.findById(id).get();
            user.setAvatarLink(storageService.getFileLink(user.getAvatar()));
            user.setBgLink(storageService.getFileLink(user.getBgImg()));
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public String updateAvatar(MultipartFile file) {
        String oldAvatar = getCurrentUser().getAvatar();
        String newAvatar = storageService.upload(file);
        if (!oldAvatar.equals("default_user_avatart.jpg"))
            storageService.deleteFile(oldAvatar);
        getCurrentUser().setAvatar(newAvatar);
        return newAvatar;
    }

    @Transactional
    public String updateBackground(MultipartFile file) {
        String oldBg = getCurrentUser().getBgImg();
        String newBg = storageService.upload(file);
        if (!oldBg.equals("default_user_bg.jpg"))
            storageService.deleteFile(oldBg);
        getCurrentUser().setBgImg(newBg);
        return newBg;
    }

    @Transactional
    public void updateAccount(String firstName, String lastName, String birthDate) {
        User user = getCurrentUser();
        if (!firstName.isEmpty())
            user.setFirstName(firstName);
        if (!lastName.isEmpty())
            user.setLastName(lastName);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate localDate = LocalDate.parse(birthDate, formatter);
            if (localDate != null) {
                user.setBirthDate(localDate);
            }
        } catch (Exception e) {
            System.out.println("Error");
        }

    }

    public void updateUserDetails(Map<String, String> data) {
        UserDetail u = userDetailRepo.findByUser(getCurrentUser());
        if (u == null) {
            u = new UserDetail();
            u.setUser(getCurrentUser());
        }
        if (isNotNullEmptyBlank(data.get("job"))) {
            u.setJob(data.get("job"));
        }
        if (isNotNullEmptyBlank(data.get("location"))) {
            u.setLocation(data.get("location"));
        }
        if (isNotNullEmptyBlank(data.get("jobLocation"))) {
            u.setJobLocation(data.get("jobLocation"));
        }
        if (isNotNullEmptyBlank(data.get("phone"))) {
            u.setPhone(data.get("phone"));
        }
        if (isNotNullEmptyBlank(data.get("intro"))) {
            u.setAbout(data.get("intro"));
        }
        if (isNotNullEmptyBlank(data.get("facebook"))) {
            u.setFacebook(data.get("facebook"));
        }
        if (isNotNullEmptyBlank(data.get("twitter"))) {
            u.setTwitter(data.get("twitter"));
        }
        if (isNotNullEmptyBlank(data.get("instagram"))) {
            u.setInstagram(data.get("instagram"));
        }
        userDetailRepo.save(u);
    }

    public boolean isNotNullEmptyBlank(String s) {
        return !(s.isEmpty() && s.isBlank());
    }

    public List<Map<String, Object>> searchUsers(String keyword) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<User> users = userRepo.search(keyword, getCurrentUser().getUserId());
        for (User user : users) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("userId", user.getUserId());
            temp.put("fullName", user.getFullName());
            temp.put("avatarLink", storageService.getFileLink(user.getAvatar()));
            result.add(temp);
        }
        return result;
    }

    public List<User> getRecommendUsers() {

        List<User> f = friendService.getListFriendByUser(getCurrentUser());
        f.add(getCurrentUser());
        List<Long> fUsers = f.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        List<User> users = userRepo.findByUserIdNotIn(fUsers);
        Collections.shuffle(users);
        List<User> result = users.subList(0, users.size() < 3 ? users.size() : 3);
        for (int i = 0; i < result.size(); i++)
            result.get(i).setAvatarLink(storageService.getFileLink(result.get(i).getAvatar()));
        return result;
    }

    public List<User> getUsersFromSessionRegistry() {
        List<User> usr = sessionRegistry.getAllPrincipals().stream()
                .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty()).map(e -> {
                    User user = null;
                    if (e instanceof CustomOAuth2User) {
                        String email = ((CustomOAuth2User) e).getEmail();
                        user = userRepo.findByEmail(email);
                    } else {
                        // String currentPrincipalName = authentication.getName();
                        String email = ((org.springframework.security.core.userdetails.User) e).getUsername();
                        user = userRepo.findByEmail(email);
                    }
                    user.setAvatarLink(storageService.getFileLink(user.getAvatar()));
                    return user;
                }).collect(Collectors.toList());
        List<User> result = new ArrayList<>(new HashSet<>(usr));
        result.remove(getCurrentUser());
        return result;
    }

}
