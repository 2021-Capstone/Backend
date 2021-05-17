package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Session {

    @Id
    @GeneratedValue
    @Column(name = "session_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "session_start_time")
    private LocalDateTime startTime;

    @Column(name = "session_end_time")
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "session")
    private List<History> historyList = new ArrayList<>();

    public void setGroup(Group group){
        this.setGroup(group);
        group.getSessionList().add(this); //어차피 한 그룹에서 수업은 하나만 할 수 있긴 함
    }
}
