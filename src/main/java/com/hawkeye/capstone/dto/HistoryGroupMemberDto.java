package com.hawkeye.capstone.dto;

import lombok.Data;

@Data
public class HistoryGroupMemberDto {

    private String name;
    private String email;
    private int attitude;
    private int absenceTime;
    private boolean isAttend;

    public HistoryGroupMemberDto(String name, String email, int attitude, int absenceTime, boolean isAttend) {
        this.name = name;
        this.email = email;
        this.attitude = attitude;
        this.absenceTime = absenceTime;
        this.isAttend = isAttend;
    }
}
