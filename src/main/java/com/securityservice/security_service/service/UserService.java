package com.securityservice.security_service.service;

import com.securityservice.security_service.model.dto.request.RegisterUserRequest;
import com.securityservice.security_service.model.dto.response.UserResponse;
import com.securityservice.security_service.model.entity.Role;
import com.securityservice.security_service.model.entity.User;
import com.securityservice.security_service.repository.RoleRepository;
import com.securityservice.security_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
