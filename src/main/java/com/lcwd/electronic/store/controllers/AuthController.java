package com.lcwd.electronic.store.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lcwd.electronic.store.dtos.ApiMessageResponse;
import com.lcwd.electronic.store.dtos.JwtRequest;
import java.util.*;
import com.lcwd.electronic.store.dtos.JwtResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.security.JwtHelper;
import com.lcwd.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private ModelMapper modelMapper;

    @Value("${googleClientId}")
    private String googleClientId;
    @Value("${newPassword}")
    private String newPassword;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserService userService;
//
    @Autowired
    private JwtHelper helper;
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request)
    {
        this.doAuthenticate(request.getEmail(),request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = helper.generateToken(userDetails);

        UserDto userDto = mapper.map(userDetails,UserDto.class);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .user(userDto)
                .build();

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                UsernamePasswordAuthenticationToken(email,password);

        try
        {
            Authentication authenticated =  authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }
        catch (Exception e)
        {
            throw new BadApiRequest("Inavalid credentials !!");
        }
    }

    //login with google api

    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException {


        //get the id token from request
        String idToken = data.get("idToken").toString();

        NetHttpTransport netHttpTransport = new NetHttpTransport();

        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));


        GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);


        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        logger.info("Payload : {}", payload);

        String email = payload.getEmail();

        User user = null;

        user = userService.findUserByEmailOptional(email).orElse(null);

        if (user == null) {
            //create new user
            user = this.saveUser(email, data.get("name").toString(), data.get("photoUrl").toString());
        }
        ResponseEntity<JwtResponse> jwtResponseResponseEntity = this.login(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());
        return jwtResponseResponseEntity;


    }

    private User saveUser(String email, String name, String photoUrl) {

        UserDto newUser = UserDto.builder()
                .name(name)
                .email(email)
                .password(newPassword)
                .userImage(photoUrl)
                .roles(new HashSet<>())
                .build();

        UserDto user = userService.createUser(newUser);

        return this.modelMapper.map(user, User.class);

    }


}
