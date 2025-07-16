package com.assignment.assignment.services;

import com.assignment.assignment.Repository.RoleRepository;
import com.assignment.assignment.Repository.UserRepository;
import com.assignment.assignment.models.ERole;
import com.assignment.assignment.models.Role;
import com.assignment.assignment.models.User;
import com.assignment.assignment.payload.Request.SignupRequest;
import com.assignment.assignment.payload.Response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @Override
    public String saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Error: Email is already in use!";
        }

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
        userRepository.flush();
        return "SSO User registered successfully!";
    }
}
