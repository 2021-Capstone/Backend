package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class YawGraph {

    @Id
    @GeneratedValue
    @Column(name = "yaw_graph_id")
    private Long id;

    @Column(name = "yaw_left")
    private float yawLeft;

    @Column(name = "yaw_normal")
    private float yawNormal;

    @Column(name = "yaw_right")
    private float yawRight;
}
