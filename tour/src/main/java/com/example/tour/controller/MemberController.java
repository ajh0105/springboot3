package com.example.tour.controller;

import com.example.tour.domain.Member;
import com.example.tour.dto.LoginDto;
import com.example.tour.dto.MemberFormDto;
import com.example.tour.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 회원 목록
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("list", memberService.findAll());
        return "member/list";
    }

    // 회원가입 폼
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/form";
    }

    // 회원가입 처리
    @PostMapping("/new")
    public String save(@Valid MemberFormDto dto,
                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "member/form";
        }

        memberService.save(dto);
        return "redirect:/member/list";
    }

    // 로그인 폼
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "member/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@ModelAttribute LoginDto dto,
                        HttpSession session,
                        Model model) {

        try {
            Member member = memberService.login(dto.getUsername(), dto.getPassword());
            session.setAttribute("loginMember", member);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "member/login";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    //Ajax 아이디 중복 체크
    @GetMapping("/check-username")
    @ResponseBody
    public boolean checkUsername(@RequestParam("username") String username) {
        System.out.println("username = " + username);
        return !memberService.existsByUsername(username);
    }
}
