package com.example.tour.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberFormDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String name;

    private String email;
}
