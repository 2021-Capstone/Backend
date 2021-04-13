package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class GuestHistory {

    @Id
    @GeneratedValue
    @Column(name = "guest_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_history_id")
    private HostHistory hostHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roll_graph_id")
    private RollGraph rollGraph;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yaw_graph_id")
    private YawGraph yawGraph;

    private boolean attendance;
}
