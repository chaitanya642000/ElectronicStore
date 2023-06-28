package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.validate.ImageNameValid;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;

    @NotBlank
    @Size(min=1,max = 30)
    private String name;

    @Email
    @NotBlank
    @Pattern(regexp = "^(?!\\.)[a-zA-Z0-9._%+-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$",message = "Invalid User email")
    private String email;

    @NotBlank
    @Size(min = 8,max = 12,message = "Invalid Password")
    private String password;

    @NotBlank
    private String gender;

    @ImageNameValid
    private String userImage;

    @Size(min = 0,max = 32,message = "Reached max limit")
    private String about;
}
