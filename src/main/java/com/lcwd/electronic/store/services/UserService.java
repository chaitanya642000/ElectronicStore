package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user,String userId);

    UserDto deleteUser(String userId);

    List<UserDto> getAllUser();

    UserDto getUserById(String Id);

    UserDto getUserByEmail(String email);

    List<UserDto>searchUser(String keyword);
}
