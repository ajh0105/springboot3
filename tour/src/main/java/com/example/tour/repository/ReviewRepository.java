package com.example.tour.repository;

import com.example.tour.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //관광지별 리뷰 조회
    List<Review> findByTourId(Long tourId);

    //평균 평점
    @Query("SELECT AVG(r.starRate) FROM Review r WHERE r.tour.id = :tourId")
    Double findAverageRating(@Param("tourId") Long tourId);

    //평점 정렬
    List<Review> findByTourIdOrderByStarRateDesc(Long tourId);
}
