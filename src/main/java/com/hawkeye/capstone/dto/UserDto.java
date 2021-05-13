package com.hawkeye.capstone.dto;

import lombok.Data;


@Data
public class UserDto {

    private String email;
    private String name;
    private String imageDir;
    private String password;

    public UserDto(String email, String name, String imageDir, String password) {
        this.email = email;
        this.name = name;
        this.imageDir = imageDir;
        this.password = password;
    }

    public UserDto(String email, String name, String imageDir) {
        this.email = email;
        this.name = name;
        this.password = imageDir;
    }

    public UserDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
