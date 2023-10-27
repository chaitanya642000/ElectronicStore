package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.Role;
import lombok.*;

import java.util.HashSet;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private String rolename;
    private String roleId;

    private HashSet<Role>roles;

}
