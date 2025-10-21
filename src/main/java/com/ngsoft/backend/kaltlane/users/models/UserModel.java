package com.ngsoft.backend.kaltlane.users.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel implements IUser{

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;
    
    private String secondlastname;

    @NotEmpty
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 12)
    private String username;

    private boolean admin;

}
