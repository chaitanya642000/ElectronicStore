package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    public ModelMapper mapper;
    @Override
    public UserDto createUser(UserDto user) {

        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        User user1 = dtoToEntity(user);
        User userSaved = userRepository.save(user1);

        UserDto userDto = entityToDto(user1);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userdto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        user.setName(userdto.getName());
        user.setAbout(userdto.getAbout());
        user.setGender(userdto.getGender());
        //user.setEmail(userdto.getEmail());
        user.setUserImage(userdto.getUserImage());
        user.setPassword(userdto.getPassword());

        User userupdated = userRepository.save(user);
        UserDto userDto = entityToDto(userupdated);

        return userDto;

    }

    @Override
    public UserDto deleteUser(String userId) {
        User user = userRepository.findById(userId).
                orElseThrow(()->new RuntimeException("User not found"));

        UserDto userdeletedDto = entityToDto(user);
        userRepository.deleteById(userId);
        return userdeletedDto;

    }

    @Override
    public List<UserDto> getAllUser() {
        List<User>users = userRepository.findAll();
        List<UserDto>userDtos = users.stream().map((User e)->entityToDto(e)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public UserDto getUserById(String Id) {

        User user = userRepository.findById(Id)
                       .orElseThrow(()->new RuntimeException("User not found with given ID"));

        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null)
        {
            throw new RuntimeException("User not found with given email");
        }
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User>users = userRepository.findByNameContaining(keyword);
        List<UserDto>userDtos = users.stream()
                .map((User user)->entityToDto(user))
                .collect(Collectors.toList());
        return userDtos;
    }

    private UserDto entityToDto(User user1) {

        UserDto dto = mapper.map(user1, UserDto.class);
        return dto;

    }

    private User dtoToEntity(UserDto userdto) {

        User user = mapper.map(userdto,User.class);
        return user;
    }
}
