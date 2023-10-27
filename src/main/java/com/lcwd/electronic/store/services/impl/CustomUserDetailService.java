package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);
    @Autowired
    public UserRepository userRepository;

    //modified it
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

           User user=  userRepository.findByEmail(username);

        return user;
    }
}
