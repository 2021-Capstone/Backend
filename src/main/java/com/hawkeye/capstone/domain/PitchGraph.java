package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Embeddable
@Getter
@Setter
public class PitchGraph {

    private int pitchUp;
    private int pitchNormal;
    private int pitchDown;

    public PitchGraph() {
    }

    public PitchGraph(int left, int normal, int right) {
        this.pitchUp = left;
        this.pitchNormal = normal;
        this.pitchDown = right;
    }
}
