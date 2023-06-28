package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiMessageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.services.UserService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    public UserService userService;


    //create a new User
    @PostMapping
    public ResponseEntity<ApiMessageResponse>createUser(@RequestBody @Valid UserDto userDto)
    {

        ApiMessageResponse response= ApiMessageResponse.builder().
                message("User Created Successfully").status(HttpStatus.CREATED).success(true).build();
         UserDto user = userService.createUser(userDto);
         return new ResponseEntity<>(response,HttpStatus.CREATED);
    }


    //GetAllUsers
    @GetMapping("/getAllUsers")
    public ResponseEntity<PageableResponse<UserDto>>getAllUsers(
            @RequestParam(value = "PageNumber",defaultValue = "0",required = false) int pageNumber ,
            @RequestParam(value = "PageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDirection",defaultValue = "ASC",required = false) String dir

    )
    {
        PageableResponse<UserDto>pageableResponse = userService.getAllUser(pageNumber,pageSize,sortBy,dir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }


    //GetUserById
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto>getUserById(@PathVariable("userId") String userId)
    {
        UserDto userDto = userService.getUserById(userId);
        return new ResponseEntity<>(userDto,HttpStatus.ACCEPTED);
    }

   //updateUser
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto>updateUserById(@PathVariable("userId") String userId,
                                                 @Valid @RequestBody UserDto dto)
    {
        UserDto userDto1 = userService.updateUser(dto,userId);
        return new ResponseEntity<>(userDto1,HttpStatus.ACCEPTED);
    }


    //deleteUserById
    @DeleteMapping("/{userId}")
    public ResponseEntity<UserDto>deleteUserById(@PathVariable("userId") String userId)
    {
        UserDto userDto = userService.deleteUser(userId);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto>getUserByEmail(@PathVariable("userEmail") String email)
    {
        UserDto userDto = userService.getUserByEmail(email);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }


    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>>getUserByKeyword(@PathVariable("keywords") String keyword)
    {
        List<UserDto>allUsers = userService.searchUser(keyword);
        return new ResponseEntity<>(allUsers,HttpStatus.ACCEPTED);
    }


}
