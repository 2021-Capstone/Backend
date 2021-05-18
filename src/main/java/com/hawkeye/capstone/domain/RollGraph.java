package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Embeddable
@Getter
@Setter
public class RollGraph {

    private float rollLeft;
    private float rollNormal;
    private float rollRight;

    public RollGraph() {
    }

    public RollGraph(float left, float normal, float right) {
        this.rollLeft = left;
        this.rollNormal = normal;
        this.rollRight = right;
    }
}
