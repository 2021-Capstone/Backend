package com.hawkeye.capstone.dto;

import com.hawkeye.capstone.domain.GroupRole;
import lombok.Data;

@Data
public class RecentTrendDto {

    private GroupRole groupRole;
    private int attendanceRate;
    private int concentrationRate;

    public RecentTrendDto(GroupRole groupRole, int attendanceRate, int concentrationRate) {
        this.groupRole = groupRole;
        this.attendanceRate = attendanceRate;
        this.concentrationRate = concentrationRate;
    }
}
