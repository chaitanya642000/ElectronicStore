package com.lcwd.electronic.store.dtos;


import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JwtRequest {
    private String email;
    private String password;

}
