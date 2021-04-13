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
public class RollGraph {

    @Id
    @GeneratedValue
    @Column(name = "roll_graph_id")
    private Long id;

    @Column(name = "roll_left")
    private float rollLeft;

    @Column(name = "roll_normal")
    private float rollNormal;

    @Column(name = "roll_right")
    private float rollRight;

}
