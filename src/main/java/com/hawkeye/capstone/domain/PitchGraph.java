package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Embeddable
@Getter
@Setter
public class PitchGraph {

    private float pitchUp;
    private float pitchNormal;
    private float pitchDown;

    public PitchGraph() {
    }

    public PitchGraph(float up, float normal, float down) {
        this.pitchUp = up;
        this.pitchNormal = normal;
        this.pitchDown = down;
    }
}
