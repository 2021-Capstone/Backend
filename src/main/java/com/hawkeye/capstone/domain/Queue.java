package com.hawkeye.capstone.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    /**
     * 연관관계 편의 메소드
     */
    public void setWaitingList(WaitingList waitingList){
        this.waitingList = waitingList;
        waitingList.getQueueList().add(this);
    }

}
