package com.securityservice.security_service.feignclients;

import com.securityservice.security_service.model.dto.request.RegisterUserRequest;
import com.securityservice.security_service.model.dto.response.UserResponse;
import com.securityservice.security_service.util.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "userservice", url = "http://localhost:8081/api/users")
public interface UserFeignClient {

    @PostMapping("/create")
    ApiResponse<UserResponse> registerUser(@RequestBody RegisterUserRequest request);
}
