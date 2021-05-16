package com.hawkeye.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupMemberSimpleDto {
    private String name;
    private String email;
}
