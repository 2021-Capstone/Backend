package com.hawkeye.capstone.dto;

import lombok.Data;


@Data
public class UserDto {

    private String email;
    private String name;
    private byte[] image;

    public UserDto(String email, String name, byte[] image) {
        this.email = email;
        this.name = name;
        this.image = image;
    }

    public UserDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
