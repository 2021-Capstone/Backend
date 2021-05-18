package com.hawkeye.capstone.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
public class History {

    @Id
    @GeneratedValue
    @Column(name = "history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "host_history_id")
//    private HostHistory hostHistory;

    @OneToMany(mappedBy = "history")
    private List<TimeLineLog> timeLineLogList = new ArrayList<>();

    private int attendanceCount;

    private int vibe;

    @Embedded
    private RollGraph rollGraph;

    @Embedded
    private YawGraph yawGraph;

    private boolean attendance;

    public int getAbsenceTime(){

        int time = 0;

        for(TimeLineLog timeLineLog : timeLineLogList){
            time += timeLineLog.getEndHour() - timeLineLog.getStartHour() * 60;
            time += timeLineLog.getEndMinute() - timeLineLog.getStartMinute();
        }

        return time;
    }
}
