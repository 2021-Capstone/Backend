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

}
