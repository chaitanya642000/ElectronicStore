package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String fullFilePath;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${normal.role.id}")
    private String normal_userID;

    @Override
    public UserDto createUser(UserDto user) {

        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User user1 = dtoToEntity(user);

        Role role = roleRepository.findById(normal_userID).get();
        HashSet<Role>roles = (HashSet<Role>) user1.getRoles();
        if(roles == null)
        {
            roles = new HashSet<Role>();
        }
        roles.add(role);
        user1.setRoles(roles);
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
        if(!user.getUserImage().equals("defaultimage.jpg"))
        {
            String fullpath = fullFilePath+File.separator+user.getUserImage();
            Path path = Paths.get(fullpath);

            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
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

        if(!user.getUserImage().equals("defaultimage.jpg"))
        {
            String fullpath = fullFilePath+ File.separator+user.getUserImage();
            Path path = Paths.get(fullpath);

            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }

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

    @Override
    public Optional<User> findUserByEmailOptional(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
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
