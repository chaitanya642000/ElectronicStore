package com.lcwd.electronic.store.dtos;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String userImage;
    private String about;
}
