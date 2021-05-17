package com.hawkeye.capstone.dto;

import lombok.Data;


@Data
public class UserDto {

    private String email;
    private String name;
    private String imageDir;
    private String imageDir2;
    private String imageDir3;
    private String password;

    public UserDto(String email, String name, String imageDir, String imageDir2, String imageDir3, String password) {
        this.email = email;
        this.name = name;
        this.imageDir = imageDir;
        this.imageDir2 = imageDir2;
        this.imageDir3 = imageDir3;
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
