package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Embeddable
@Getter
@Setter
public class RollGraph {

    private int rollLeft;
    private int rollNormal;
    private int rollRight;

    public RollGraph() {
    }

    public RollGraph(int left, int normal, int right) {
        this.rollLeft = left;
        this.rollNormal = normal;
        this.rollRight = right;
    }
}
