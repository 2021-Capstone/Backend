package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class WaitingList {

    @Id
    @GeneratedValue
    @Column(name = "waiting_list_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    private int count;

    @OneToMany(mappedBy = "waitingList")
    private List<Queue> queueList;
}
