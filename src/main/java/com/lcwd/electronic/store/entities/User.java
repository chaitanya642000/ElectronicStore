package com.lcwd.electronic.store.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@ToString
@Table(name="users")
public class User {

    @Id
    private String userId;
    private String name;
    @Column(name="user_email",unique = true)
    private String email;

    @Column(name="user_password",length = 10)
    private String password;
    @Column(name="user_gender")
    private String gender;

    @Column(name="user_image")
    private String userImage;
    private String about;


}
