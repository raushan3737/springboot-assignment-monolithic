package com.zemoso.easycart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Generated
public class UserDTO {
    private Long id;

    private String username;

    private String email;

    private String password;

}
