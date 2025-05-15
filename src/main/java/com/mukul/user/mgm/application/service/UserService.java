package com.mukul.user.mgm.application.service;

import com.mukul.user.mgm.application.exception.UserException;
import com.mukul.user.mgm.application.model.User;
import com.mukul.user.mgm.application.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Retrieve all users
    public List<User> getAllUsers() throws UserException {
        List<User> users = userRepository.findAll();
        if (CollectionUtils.isEmpty(users)) {
            throw new UserException("No users found");
        }
        return users;
    }

    // Retrieve a user by userName
    public User getUserByUserName(String userName) throws UserException {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new UserException("User not found with userName: " + userName);
        }
        return user;
    }

    // Create a new user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Update an existing user's details
    public User updateUser(String userName, User updatedUser) throws UserException {
        User existingUser = userRepository.findByUserName(userName);
        if (existingUser == null) {
            throw new UserException("User not found with userName: " + userName);
        }

        // Update mutable fields (e.g., password and role)
        existingUser.setPassWord(updatedUser.getPassWord());
        existingUser.setRole(updatedUser.getRole());

        return userRepository.save(existingUser);
    }

    // Delete a user by their userName
    public void deleteUser(String userName) throws UserException {
        User existingUser = userRepository.findByUserName(userName);
        if (existingUser == null) {
            throw new UserException("User not found with userName: " + userName);
        }
        userRepository.delete(existingUser);
    }

    // Check if a user exists by userName
    public boolean userExists(String userName) {
        return userRepository.findByUserName(userName) != null;
    }
}
