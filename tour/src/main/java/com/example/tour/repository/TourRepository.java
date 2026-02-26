package com.example.tour.repository;

import com.example.tour.domain.Tour;
import com.example.tour.domain.TourCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {

    //카테고리 검색
    List<Tour> findByCategory(TourCategory category);
}
