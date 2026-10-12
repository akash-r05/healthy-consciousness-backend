package com.healthyconsciousness.backend.service;

import com.healthyconsciousness.backend.dto.auth.AuthResponse;
import com.healthyconsciousness.backend.dto.auth.LoginRequest;
import com.healthyconsciousness.backend.dto.auth.RegisterRequest;
import com.healthyconsciousness.backend.entity.User;
import com.healthyconsciousness.backend.entity.UserRole;
import com.healthyconsciousness.backend.exception.ApiException;
import com.healthyconsciousness.backend.repository.UserRepository;
import com.healthyconsciousness.backend.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      AuthenticationManager authenticationManager,
                      JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim();
        if (userRepository.existsByEmail(email)) {
            throw new ApiException("User already exists for email: " + email);
        }

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(request.password()))
                .firstName(request.firstName().trim())
                .lastName(request.lastName().trim())
                .phoneNumber(request.phoneNumber())
                .bio(request.bio())
                .role(UserRole.USER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        String token = jwtTokenProvider.generateToken(user.getEmail());

        return new AuthResponse(token, "Bearer", user.getEmail(), user.getRole().name(), user.isActive());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException("Invalid email or password"));

        if (!user.isActive()) {
            throw new ApiException("Account is disabled or not activated");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail());
        return new AuthResponse(token, "Bearer", user.getEmail(), user.getRole().name(), true);
    }

    @Transactional
    public void activateAccountByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found with email: " + email));
        user.setActive(true);
        userRepository.save(user);
    }
}
