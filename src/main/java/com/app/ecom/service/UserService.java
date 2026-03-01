package com.app.ecom.service;

import com.app.ecom.repository.UserRepository;
import com.app.ecom.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;


    Long userId = 1L;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> fetchAllUsers() {

        List<User> userList = userRepository.findAll();
        return userList;
    }

    public void addUser(User user) {
        //  user.setId(userId++);
        userRepository.save(user);
    }

    public Optional<User> getUserByUserId(@PathVariable Long id) {

        return userRepository.findById(id);

    }

    public Optional<User> updateUser(Long id, User user) {

         return userRepository.findById(id).
                map(existinguser -> {
                 existinguser.setFirstName(user.getFirstName());
                existinguser.setLastName(user.getLastName());
                existinguser.setUpdatedAt(user.getUpdatedAt());
                existinguser.setCreatedDate(user.getCreatedDate());
                existinguser.setEmail(user.getEmail());
                existinguser.setPhone(user.getPhone());
                existinguser.setAddress(user.getAddress());
                existinguser.setRole(user.getRole());
                return userRepository.save(existinguser);
                });

    }
}
