package com.hawkeye.capstone.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "history")
    private List<TimeLineLog> timeLineLogList;

    private int attendanceCount;

    private int attitude;

    private int vibe;

    @Embedded
    private RollGraph rollGraph;

    @Embedded
    private YawGraph yawGraph;

    private boolean isAttend;

    public int getAbsenceTime(){

        int time = 0;

        for(TimeLineLog timeLineLog : timeLineLogList){
            if(timeLineLog.getState() == "absence"){
                time += timeLineLog.getEndHour() - timeLineLog.getStartHour() * 60;
                time += timeLineLog.getEndMinute() - timeLineLog.getStartMinute();
            }
        }

        return time;
    }
}
