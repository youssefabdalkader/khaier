package com.example.khaier.service.Impl;

import com.example.khaier.dto.request.UserLoginDto;
import com.example.khaier.dto.request.UserRegistrationDto;
import com.example.khaier.entity.User;
import com.example.khaier.enums.Gender;
import com.example.khaier.enums.Role;
import com.example.khaier.repository.UserRepository;
import com.example.khaier.security.JwtTokenUtils;
import com.example.khaier.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    public User registerUser(UserRegistrationDto registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new RuntimeException("Email address already in use.");
        }

        if(!registerRequest.password().equals(registerRequest.confirmPassword())){
            throw new RuntimeException("passwords isn't match");
        }

        // Create a new user
        User user = User.builder()
                .username(registerRequest.username())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .userRole(Role.ROLE_USER)
                .userGender(registerRequest.userGender())
                .phone(registerRequest.userPhone())
                .build();

        return userRepository.save(user);
    }

    public String loginUser(UserLoginDto loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequest.email()));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return jwtTokenUtils.generateToken(user, "loginToken");
    }
}
