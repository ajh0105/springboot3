package com.example.tour.controller;

import com.example.tour.dto.ReviewFormDto;
import com.example.tour.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/write")
    public String save(@Valid ReviewFormDto dto) {
        reviewService.save(dto);
        return "redirect:/tour/" + dto.getTourId();
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, @Valid ReviewFormDto dto, Model model) {
        reviewService.save(dto);
        return "redirect:/tour/" + dto.getTourId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        reviewService.delete(id);
        return "redirect:/tour/list";
    }
}
