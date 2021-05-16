package com.hawkeye.capstone.dto;

import lombok.Data;

@Data
public class WaitingMemberDto {
    private String name;
    private String email;

    public WaitingMemberDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
