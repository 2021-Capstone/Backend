package com.hawkeye.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnAirDto {

    Long sessionId;
    boolean isOnAir;

}
