package com.hawkeye.capstone.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    private float attitude;

    private int vibe;

    private boolean isAttend;

    @Embedded
    private PitchGraph pitchGraph;

    @Embedded
    private YawGraph yawGraph;

}
