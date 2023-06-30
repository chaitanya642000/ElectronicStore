package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiMessageResponse;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public UserService userService;

    @Autowired
    public FileService fileService;

    @Value("${user.profile.defaultimage.path}")
    private String defaultUserImage;

    @Value("${user.profile.image.path}")
    private String uploadImagePath;

    //create a new User
    @PostMapping
    public ResponseEntity<ApiMessageResponse>createUser(@RequestBody @Valid UserDto userDto)
    {
        logger.info(String.valueOf(userDto));
        if(userDto.getUserImage() == null || userDto.getUserImage().equals(""))
        {
            userDto.setUserImage(defaultUserImage);
        }
        UserDto user = userService.createUser(userDto);
        ApiMessageResponse response= ApiMessageResponse.builder().
                message("User Created Successfully").status(HttpStatus.CREATED).success(true).build();
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


    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse>uploadUserImage(
            @PathVariable("userId") String userId,
            @RequestParam("userImage") MultipartFile image
            ) throws IOException {

         String fileName = fileService.uploadFile(image,uploadImagePath);

         UserDto user = userService.getUserById(userId);
         user.setUserImage(fileName);
         userService.updateUser(user,userId);

         ImageResponse response = ImageResponse.builder().imageName(fileName).success(true)
                 .message("Image uploaded successfully").status(HttpStatus.CREATED).build();

         return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    /*
     * Controller to serve user image
     * */
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable("userId") String userId,
                               HttpServletResponse response) throws IOException {
        UserDto user = userService.getUserById(userId);
        logger.info("User image is "+user.getUserImage());


        InputStream resource =  fileService.getResource(uploadImagePath,user.getUserImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }

}
