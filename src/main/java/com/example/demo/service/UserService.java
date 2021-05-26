package com.example.demo.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import com.example.demo.model.CustomOAuth2User;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Tên đăng nhập không tồn tại");
        }
        if (!user.isEnable())
            throw new IllegalStateException("Email chưa được xác thực");
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
            throw new IllegalStateException("Email đã được đăng kí");
        }
        user.setVerificationCode(RandomString.make(64));
        sendVerificationCode(user, siteURL);
        userRepo.save(user);
    }

    private void sendVerificationCode(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "bankhongphailanguoimay@gmail.com";
        String senderName = "DM Social Network";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Your company name.";

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

    @Transactional
    public boolean verifyUser(String code) {
        User user = userRepo.findByVerificationCode(code);
        if (user == null || user.isEnable())
            return false;
        user.setVerificationCode(null);
        user.setEnable(true);
        return true;
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public void saveUserOAuth(String email, String fullName, String provider) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode("OAuth2"));
        newUser.setFirstName(fullName);
        newUser.setLastName("");
        newUser.setEnable(true);
        newUser.setAuthProvider(provider);
        userRepo.save(newUser);
    }

    public void updateUserAfterOauthLogin(User user, String fullName, String oauthName) {
        user.setFirstName(fullName);
        user.setLastName("");
        user.setAuthProvider(oauthName);
        userRepo.save(user);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomOAuth2User) {
            String email = ((CustomOAuth2User) principal).getEmail();
            return userRepo.findByEmail(email);
        }
        String currentPrincipalName = authentication.getName();
        return userRepo.findByEmail(currentPrincipalName);
    }
}
