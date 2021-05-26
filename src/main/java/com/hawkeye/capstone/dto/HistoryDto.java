package com.hawkeye.capstone.dto;

import com.hawkeye.capstone.domain.GroupRole;
import com.hawkeye.capstone.domain.PitchGraph;
import com.hawkeye.capstone.domain.TimeLineLog;
import com.hawkeye.capstone.domain.YawGraph;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HistoryDto {

    private GroupRole role;
    private Long historyId;
    private String groupName;
    private int createdYear;
    private int createdMonth;
    private int createdDay;
    private int attendanceCount;
    private int vibe;
    private int attitude;
    private boolean isAttend;
    private List<TimeLineLog> timeLineLogList = new ArrayList<>();
    private PitchGraph roll;
    private YawGraph yaw;
    private List<HistoryGroupMemberDto> historyGroupMemberDtoList = new ArrayList<>();

    //GUEST 생성자

    public HistoryDto(GroupRole role, Long historyId, String groupName, int createdYear,
                      int createdMonth, int createdDay, int attendanceCount, int vibe, int attitude,
                      boolean isAttend, List<TimeLineLog> timeLineLogList,
                      PitchGraph roll, YawGraph yaw) {
        this.role = role;
        this.historyId = historyId;
        this.groupName = groupName;
        this.createdYear = createdYear;
        this.createdMonth = createdMonth;
        this.createdDay = createdDay;
        this.attendanceCount = attendanceCount;
        this.vibe = vibe;
        this.attitude = attitude;
        this.isAttend = isAttend;
        this.timeLineLogList = timeLineLogList;
        this.roll = roll;
        this.yaw = yaw;
    }

    //HOST 생성자

    public HistoryDto(GroupRole role, Long historyId, String groupName,
                      int createdYear, int createdMonth, int createdDay, int attendanceCount, int vibe,
                      List<HistoryGroupMemberDto> historyGroupMemberDtoList) {
        this.role = role;
        this.historyId = historyId;
        this.groupName = groupName;
        this.createdYear = createdYear;
        this.createdMonth = createdMonth;
        this.createdDay = createdDay;
        this.attendanceCount = attendanceCount;
        this.vibe = vibe;
        this.historyGroupMemberDtoList = historyGroupMemberDtoList;
    }
}
