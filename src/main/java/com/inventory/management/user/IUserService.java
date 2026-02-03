package com.inventory.management.user;

import java.util.List;

public interface IUserService {
    UserResponse createUser(UserCreateRequest request);
    List<UserResponse> getAllUsers();
}
