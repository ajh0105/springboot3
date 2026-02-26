package com.example.tour.dto;

import com.example.tour.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private String username;
    private String comment;
    private Double starRate;

    public ReviewResponseDto(Review review) {
        this.username = review.getMember().getUsername();
        this.comment = review.getComment();
        this.starRate = review.getStarRate();
    }
}
