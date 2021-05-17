package com.hawkeye.capstone.dto;

import lombok.Data;

@Data
public class HostHistoryDto {

    private String userName;
    private String email;
    private int vibe;
    private int absenceTime;
    private boolean attendance;

    public HostHistoryDto(String userName, String email, int vibe, int absenceTime, boolean attendance) {
        this.userName = userName;
        this.email = email;
        this.vibe = vibe;
        this.absenceTime = absenceTime;
        this.attendance = attendance;
    }
}
