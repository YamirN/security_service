package com.securityservice.security_service.model.dto.response;


public class UserResponse {

    private Long userId;
    private String username;
    private String role;
    private boolean enabled;

    public UserResponse(Long userId, String username, String role, boolean enabled) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
