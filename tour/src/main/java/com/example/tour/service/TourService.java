package com.example.tour.service;

import com.example.tour.config.FileConfig;
import com.example.tour.domain.Tour;
import com.example.tour.dto.TourFormDto;
import com.example.tour.dto.TourResponseDto;
import com.example.tour.repository.ReviewRepository;
import com.example.tour.repository.TourRepository;
import com.example.tour.util.FileUploadUtil;
import com.example.tour.util.RatingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TourService {

    private final TourRepository tourRepository;
    private final ReviewRepository reviewRepository;
    private final FileConfig fileConfig;

    //관광지 추가
    public Long save(TourFormDto dto) throws Exception {
        String fileName = FileUploadUtil.saveFile(fileConfig.uploadDir, dto.getFile());
        //DTO => Entity(Builder)
        Tour tour = Tour.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .address(dto.getAddress())
                .tel(dto.getTel())
                .comment(dto.getComment())
                .thumb(fileName)
                .trafficMap(dto.getTrafficMap())
                .tourMap(dto.getTourMap())
                .foodMap(dto.getFoodMap())
                .bedMap(dto.getBedMap())
                .build();

        return tourRepository.save(tour).getId();
    }

    //관광지 수정 : DTO => Entity(Getter/Setter)
    public void update(Long id, TourFormDto dto) throws Exception {
        Tour tour = tourRepository.findById(id).orElseThrow();

        if(dto.getFile() != null && !dto.getFile().isEmpty()) {
            String fileName = FileUploadUtil.saveFile(fileConfig.uploadDir, dto.getFile());
            tour.setThumb(fileName);
        }

        tour.setName(dto.getName());
        tour.setCategory(dto.getCategory());
        tour.setAddress(dto.getAddress());
        tour.setTel(dto.getTel());
        tour.setComment(dto.getComment());
        tour.setTrafficMap(dto.getTrafficMap());
        tour.setTourMap(dto.getTourMap());
        tour.setFoodMap(dto.getFoodMap());
        tour.setBedMap(dto.getBedMap());
    }

    //관광지 목록
    public List<TourResponseDto> findAll() {
        //Entity => DTO
        return tourRepository.findAll().stream().map(
                tour -> {
                    Double avg = reviewRepository.findAverageRating(tour.getId());
                    return new TourResponseDto(tour, RatingUtil.round(avg));
                }
        ).collect(Collectors.toList());
    }

    //관광지 상세 정보
    @Transactional
    public Tour findById(Long id) {  //TourResponseDto로 반환하는 경우: Entity => DTO
        return tourRepository.findById(id).orElseThrow();
    }

    //관광지 삭제
    public void delete(Long id) {
        tourRepository.deleteById(id);
    }
}
