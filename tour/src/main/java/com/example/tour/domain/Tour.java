package com.example.tour.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tour")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tour extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TourCategory category;

    @Column(nullable = false, length = 100)
    private String name;

    private String thumb;

    private String address;

    private String tel;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "traffic_map")
    private String trafficMap;

    @Column(name = "tour_map")
    private String tourMap;

    @Column(name = "food_map")
    private String foodMap;

    @Column(name = "bed_map")
    private String bedMap;

    // 리뷰 (ON DELETE CASCADE 대응)
    @OneToMany(mappedBy = "tour",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
}
