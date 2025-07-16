package com.assignment.assignment.services;

import com.assignment.assignment.models.User;
import com.assignment.assignment.payload.Request.SignupRequest;

public interface UserService {
    String saveUser(User user);
}
