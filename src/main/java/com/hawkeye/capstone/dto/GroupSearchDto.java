package com.hawkeye.capstone.dto;

import com.hawkeye.capstone.domain.GroupRole;
import lombok.Data;

@Data
public class GroupSearchDto {

    private String name;
    private String enterCode;
    private boolean isOnAir;
    private GroupRole role;

    public GroupSearchDto(String name, String enterCode, boolean isOnAir, GroupRole role) {
        this.name = name;
        this.enterCode = enterCode;
        this.isOnAir = isOnAir;
        this.role = role;
    }
}
