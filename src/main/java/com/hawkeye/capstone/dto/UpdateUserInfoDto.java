package com.hawkeye.capstone.dto;


import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserInfoDto {


    @NotNull
    private String email;

    @NotNull
    private String name;
}
