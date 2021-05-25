package com.example.demo.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority("Role_User")));
    }

    public void registerUser(Map<String, Object> data) throws IllegalStateException {
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
                user.setPassword(value);
            }
            if (key.equals("birthDate")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                user.setBirthDate(LocalDate.parse(value, formatter));
            }
        }
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email đã được đăng kí");
        }
        userRepo.save(user);
    }
}
