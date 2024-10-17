package com.user_management.userservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.user_management.userservice.model.User;
import com.user_management.userservice.reporistory.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Get all users
     * 
     * @return List of users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user by Id
     * 
     * @param userId
     * @return Unique user
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * Create new user
     * 
     * @param user
     * @return Created user
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Update user using the Id, not all propreties can be updated
     * 
     * @param userId
     * @param user
     * @return User object updated
     */
    public User updateUser(Long userId, User user) {
        Optional<User> userToUpdate = userRepository.findById(userId);
        if (userToUpdate.isPresent()) {
            User existingUser = userToUpdate.get();
            existingUser.setPassword(user.getPassword());
            existingUser.setEmail(user.getEmail());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(existingUser);
        }
        return null;
    }

    /**
     * Delete user by Id
     * 
     * @param userId
     * @return if the delete success
     */
    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    /**
     * Activate a user by Id
     * 
     * @param userId
     * @return if the change success
     */
    public boolean activateUser(Long userId) {
        if (userRepository.existsById(userId)) {
            User user = userRepository.findById(userId).orElse(null);
            user.setIsActive(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * Deactivate a user by Id
     * 
     * @param userId
     * @return if the change succuess
     */
    public boolean deactivateUser(Long userId) {
        if (userRepository.existsById(userId)) {
            User user = userRepository.findById(userId).orElse(null);
            user.setIsActive(false);
            return true;
        }
        return false;
    }

    /**
     * Register in new user with hashing the password
     * 
     * @param user
     * @return New user created
     */
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password
        return userRepository.save(user);
    }

    /**
     * Authenticate user by username and password
     * 
     * @param username
     * @param password
     * @return Message of authentication state
     */
    public User authenticate(String username, String password) {
        try {
            // Attempt authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // If successful, return a success message or a token
            return (User) authentication.getPrincipal();

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    /**
     * Find user by username
     * 
     * @param username
     * @return User object
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
