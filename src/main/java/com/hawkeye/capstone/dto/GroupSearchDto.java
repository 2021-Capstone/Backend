package com.hawkeye.capstone.dto;

import com.hawkeye.capstone.domain.GroupRole;
import lombok.Data;

@Data
public class GroupSearchDto {

    private String name;
    private String enterCode;
    private boolean isOnAir;
    private GroupRole role;
    private Long id;

    public GroupSearchDto(String name, String enterCode, boolean isOnAir, GroupRole role, Long id) {
        this.name = name;
        this.enterCode = enterCode;
        this.isOnAir = isOnAir;
        this.role = role;
        this.id = id;
    }
}
