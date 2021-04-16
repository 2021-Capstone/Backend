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

    public YawGraph(float yawLeft, float yawNormal, float yawRight) {
        this.yawLeft = yawLeft;
        this.yawNormal = yawNormal;
        this.yawRight = yawRight;
    }
}
