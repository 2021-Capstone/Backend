package com.hawkeye.capstone.dto;

import lombok.Data;


@Data
public class UserDto {

    private String email;
    private String name;
    //private byte[] image;
    private String imageDir;

    public UserDto(String email, String name, String imageDir) {
        this.email = email;
        this.name = name;
        this.imageDir = imageDir;
    }

    public UserDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
