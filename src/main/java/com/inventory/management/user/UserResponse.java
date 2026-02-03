package com.inventory.management.user;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        boolean active,
        String role,
        String branch,
        LocalDateTime createdAt
) {}
