package com.healthyconsciousness.backend.service;

import com.healthyconsciousness.backend.dto.auth.PasswordChangeRequest;
import com.healthyconsciousness.backend.dto.auth.ProfileUpdateRequest;
import com.healthyconsciousness.backend.dto.auth.UserResponse;
import com.healthyconsciousness.backend.entity.User;
import com.healthyconsciousness.backend.exception.ApiException;
import com.healthyconsciousness.backend.exception.ResourceNotFoundException;
import com.healthyconsciousness.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public UserResponse updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = findUserById(userId);
        user.setFirstName(request.firstName() == null ? user.getFirstName() : request.firstName().trim());
        user.setLastName(request.lastName() == null ? user.getLastName() : request.lastName().trim());
        user.setPhoneNumber(request.phoneNumber() == null ? user.getPhoneNumber() : request.phoneNumber().trim());
        user.setBio(request.bio() == null ? user.getBio() : request.bio());
        userRepository.save(user);
        return toUserResponse(user);
    }

    @Transactional
    public void changePassword(Long userId, PasswordChangeRequest request) {
        User user = findUserById(userId);
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new ApiException("Current password is incorrect");
        }
        if (request.newPassword() == null || request.newPassword().length() < 8) {
            throw new ApiException("New password must be at least 8 characters");
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getBio(),
                user.getRole().name(),
                user.isActive()
        );
    }
}
