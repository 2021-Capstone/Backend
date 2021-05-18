package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Embeddable
@Getter
@Setter
public class YawGraph {

    private float yawLeft;
    private float yawNormal;
    private float yawRight;

    public YawGraph() {
    }

    public YawGraph(float left, float normal, float right) {
        this.yawLeft = left;
        this.yawNormal = normal;
        this.yawRight = right;
    }
}
