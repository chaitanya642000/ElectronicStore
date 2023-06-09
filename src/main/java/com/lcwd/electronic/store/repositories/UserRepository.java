package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface UserRepository extends JpaRepository<User,String> {
    User findByEmail(String email);

    List<User> findByNameContaining(String keyword);

}
