package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user,String userId);

    UserDto deleteUser(String userId);

    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    UserDto getUserById(String Id);

    UserDto getUserByEmail(String email);

    List<UserDto>searchUser(String keyword);

    Optional<User> findUserByEmailOptional(String email);
}
