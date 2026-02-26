package com.example.tour.controller;

import com.example.tour.domain.Tour;
import com.example.tour.domain.TourCategory;
import com.example.tour.dto.ReviewFormDto;
import com.example.tour.dto.TourFormDto;
import com.example.tour.service.ReviewService;
import com.example.tour.service.TourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tour")
public class TourController {

    private final TourService tourService;
    private final ReviewService reviewService;

    //관광지 목록
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("list", tourService.findAll());
        return "tour/list";
    }

    //관광지 상세보기
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Tour tour = tourService.findById(id);
        model.addAttribute("tour", tour);
        model.addAttribute("reviews", reviewService.findByTour(id));
        model.addAttribute("reviewForm", new ReviewFormDto());
        return "tour/detail";
    }

    //관광지 등록 폼 로딩
    @GetMapping("/new")
    public String tourForm(Model model) {
        model.addAttribute("tourFormDto", new TourFormDto());
        model.addAttribute("categories", TourCategory.values());
        return "tour/form";
    }

    //관광지 등록 처리
    @PostMapping("/new")
    public String write(@Valid @ModelAttribute TourFormDto dto,
                        BindingResult bindingResult,
                        Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("categories", TourCategory.values());
            return "tour/form";
        }
        try {
            tourService.save(dto);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return "redirect:/tour/list";
    }

    //관광지 정보 수정 폼: Tour(Entity) => TourFormDto
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {

        Tour tour = tourService.findById(id); //Entity

        TourFormDto dto = new TourFormDto();   //Dto
        dto.setId(tour.getId());
        dto.setName(tour.getName());
        dto.setCategory(tour.getCategory());
        dto.setAddress(tour.getAddress());
        dto.setTel(tour.getTel());
        dto.setComment(tour.getComment());
        dto.setTrafficMap(tour.getTrafficMap());
        dto.setTourMap(tour.getTourMap());
        dto.setFoodMap(tour.getFoodMap());
        dto.setBedMap(tour.getBedMap());

        model.addAttribute("tourFormDto", dto);
        model.addAttribute("categories", TourCategory.values());

        return "tour/form";
    }

    //관광지 수정 처리
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute TourFormDto dto) {

        try {
            tourService.update(id, dto);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return "redirect:/tour/detail/"+id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        tourService.delete(id);
        return "redirect:/tour/list";
    }

}