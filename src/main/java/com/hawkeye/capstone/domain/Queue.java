package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Queue {

    @Id
    @GeneratedValue
    @Column(name = "queue_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiting_list_id")
    private WaitingList waitingList;

    @Enumerated(EnumType.STRING)
    private WaitingStatus status;
}
