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

    public RollGraph(float rollLeft, float rollNormal, float rollRight) {
        this.rollLeft = rollLeft;
        this.rollNormal = rollNormal;
        this.rollRight = rollRight;
    }
}
