package com.example.tour.service;

import com.example.tour.domain.Member;
import com.example.tour.domain.Review;
import com.example.tour.domain.Tour;
import com.example.tour.dto.ReviewFormDto;
import com.example.tour.dto.ReviewResponseDto;
import com.example.tour.repository.MemberRepository;
import com.example.tour.repository.ReviewRepository;
import com.example.tour.repository.TourRepository;
import com.example.tour.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final TourRepository tourRepository;
    private final MemberRepository memberRepository;

    public Long save(ReviewFormDto dto) {

        ValidationUtil.validateRating(dto.getStarRate());

        Tour tour = tourRepository.findById(dto.getTourId()).orElseThrow();
        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow();

        Review review = Review.builder()
                .tour(tour)
                .member(member)
                .comment(dto.getComment())
                .starRate(dto.getStarRate())
                .build();

        return reviewRepository.save(review).getId();
    }

    //관광지별 리뷰 목록
    public List<ReviewResponseDto> findByTour(Long tourId) {

        return reviewRepository.findByTourIdOrderByStarRateDesc(tourId)
                .stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    //특정 리뷰 상세 보기
    public Review findById(Long id) {
        return reviewRepository.findById(id).orElseThrow();
    }

    //특정 리뷰 삭제
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

}
