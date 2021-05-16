package com.hawkeye.capstone.dto;

import com.hawkeye.capstone.domain.GroupRole;
import lombok.Data;

@Data
public class RecentTrendDto {

    private GroupRole groupRole;
    private int attendanceRate;
    private int concentrationRate;
    private int drowsinessRate;

    public RecentTrendDto(GroupRole groupRole, int attendanceRate, int concentrationRate, int drowsinessRate) {
        this.groupRole = groupRole;
        this.attendanceRate = attendanceRate;
        this.concentrationRate = concentrationRate;
        this.drowsinessRate = drowsinessRate;
    }
}
