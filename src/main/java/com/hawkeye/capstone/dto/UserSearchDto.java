package com.hawkeye.capstone.dto;

import lombok.Data;

@Data
public class UserSearchDto {

    private String email;
    private String name;
    private String imageDir;

    public UserSearchDto(String email, String name, String imageDir) {
        this.email = email;
        this.name = name;
        this.imageDir = imageDir;
    }
}
