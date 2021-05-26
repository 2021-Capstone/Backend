package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Embeddable
@Getter
@Setter
public class YawGraph {

    private int yawLeft;
    private int yawNormal;
    private int yawRight;

    public YawGraph() {
    }

    public YawGraph(int left, int normal, int right) {
        this.yawLeft = left;
        this.yawNormal = normal;
        this.yawRight = right;
    }
}
