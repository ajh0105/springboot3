package com.example.tour.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewFormDto {

    private Long tourId;
    private Long memberId;

    @NotBlank
    private String comment;

    @Min(1)
    @Max(5)
    private Double starRate;
}
