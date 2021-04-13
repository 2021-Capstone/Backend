package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TimeLineLog {

    @Id
    @GeneratedValue
    @Column(name = "time_line_log_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_history_id")
    private GuestHistory guestHistory;

    @Column(name = "log_start_time")
    private LocalDateTime startTime;

    @Column(name = "log_end_time")
    private LocalDateTime endTime;
}
