package com.example.tour.dto;

import com.example.tour.domain.Tour;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourResponseDto {

    private Long id;
    private String name;
    private String category;
    private String thumb;
    private String address;
    private String tel;

    private Double avgRating;

    public TourResponseDto(Tour tour, Double avgRating) {
        this.id = tour.getId();
        this.name = tour.getName();
        this.category = tour.getCategory().name();
        this.thumb = tour.getThumb();
        this.address = tour.getAddress();
        this.tel = tour.getTel();
        this.avgRating = avgRating;
    }
}
