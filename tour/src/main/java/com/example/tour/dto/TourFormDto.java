package com.example.tour.dto;

import com.example.tour.domain.TourCategory;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
//사용자 (Request)-> 컨트롤러
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourFormDto {

    private Long id;

    @NotBlank
    private String name;
    private TourCategory category;

    private String address;
    private String tel;

    private String comment;

    private MultipartFile file;

    private String trafficMap;
    private String tourMap;
    private String foodMap;
    private String bedMap;
}
