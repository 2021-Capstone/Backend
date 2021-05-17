package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class TimeLineLog {

    @Id
    @GeneratedValue
    @Column(name = "time_line_log_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private History history;

    private String state;

    @Column(name = "log_start_hour")
    private int startHour;

    @Column(name = "log_start_minute")
    private int startMinute;

    @Column(name = "log_start_second")
    private int startSecond;

    @Column(name = "log_end_hour")
    private int endHour;

    @Column(name = "log_end_minute")
    private int endMinute;

    @Column(name = "log_end_second")
    private int endSecond;

}
