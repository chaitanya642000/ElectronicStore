package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
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
                orElseThrow(()->new ResourceNotFoundException("User not found"));

        UserDto userdeletedDto = entityToDto(user);
        userRepository.deleteById(userId);
        return userdeletedDto;

    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber ,int pageSize,String sortBy,String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()): (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);

        PageableResponse<UserDto>pageableResponse = Helper.getPageableResponse(page,UserDto.class);

//        List<User>users = page.getContent();
//        List<UserDto>userDtos = users.stream().map((User e)->entityToDto(e)).collect(Collectors.toList());
//
//        PageableResponse<UserDto>pageableResponse = new PageableResponse<>();
//        pageableResponse.setContent(userDtos);
//        pageableResponse.setPageSize(page.getSize());
//        pageableResponse.setPageNumber(page.getNumber());
//        pageableResponse.setGetTotalElements(page.getTotalElements());
//        pageableResponse.setTotalPages(page.getTotalPages());
//        pageableResponse.setIsLastPage(page.isLast());


        return pageableResponse;
    }

    @Override
    public UserDto getUserById(String Id) {

        User user = userRepository.findById(Id)
                       .orElseThrow(()->new ResourceNotFoundException("User not found with given ID"));

        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null)
        {
            throw new ResourceNotFoundException("User not found with given email");
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
