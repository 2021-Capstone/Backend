package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Group {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "group_name")
    private String name;

    @Column(name = "group_enter_code")
    private String code;

    @Column(name = "absence_time_setting")
    private int absenceTime;

    @Column(name = "alert_duration")
    private int alertDuration;

    @Column(name = "is_on_air")
    private boolean onAir;

    @OneToMany(mappedBy = "group")
    private WaitingList waitingList;

    @OneToMany(mappedBy = "group")
    private Session session;
}
