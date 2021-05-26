package com.hawkeye.capstone.dto;

import com.hawkeye.capstone.domain.GroupRole;
import com.hawkeye.capstone.domain.PitchGraph;
import com.hawkeye.capstone.domain.TimeLineLog;
import com.hawkeye.capstone.domain.YawGraph;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GuestHistoryDto {

    private Long id;
    private GroupRole role;
    private String groupName;
    private LocalDateTime createdAt;
    private int attendanceCount;
    private int vibe;
    private boolean isAttendance;
    private List<TimeLineLog> timeLineLogList = new ArrayList<>();
    private PitchGraph roll;
    private YawGraph yaw;

    //게스트용 HistoryDto
    public GuestHistoryDto(Long id, GroupRole role, String groupName, LocalDateTime createdAt, int attendanceCount,
                           int vibe, boolean isAttendance, List<TimeLineLog> timeLineLogList, PitchGraph roll, YawGraph yaw) {
        this.id = id;
        this.role = role;
        this.groupName = groupName;
        this.createdAt = createdAt;
        this.attendanceCount = attendanceCount;
        this.vibe = vibe;
        this.isAttendance = isAttendance;
        this.timeLineLogList = timeLineLogList;
        this.roll = roll;
        this.yaw = yaw;
    }

}
