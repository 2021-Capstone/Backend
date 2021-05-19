package com.hawkeye.capstone.dto;

import lombok.Data;

@Data
public class UserSearchDto {

    private String email;
    private String name;
    private String imageDir1;
    private String imageDir2;
    private String imageDir3;

    public UserSearchDto(String email, String name, String imageDir1, String imageDir2, String imageDir3) {
        this.email = email;
        this.name = name;
        this.imageDir1 = imageDir1;
        this.imageDir2 = imageDir2;
        this.imageDir3 = imageDir3;
    }

    public UserSearchDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
