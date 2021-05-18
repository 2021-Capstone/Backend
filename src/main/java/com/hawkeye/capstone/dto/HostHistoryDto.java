package com.hawkeye.capstone.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class HostHistoryDto {

    @JsonIgnore
    private Long id;
    private String userName;
    private String email;
    private int vibe;
    private int absenceTime;
    private boolean attendance;

    public HostHistoryDto(Long id, String userName, String email, int vibe, int absenceTime, boolean attendance) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.vibe = vibe;
        this.absenceTime = absenceTime;
        this.attendance = attendance;
    }
}
