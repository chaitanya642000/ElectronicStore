package com.lcwd.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtResponse {
    private String jwtToken;
    private UserDto user;
}
