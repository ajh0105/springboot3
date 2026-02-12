package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.BoardService;
import com.example.demo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final BoardService boardService;

    @GetMapping("/login")
    public String loginForm(@RequestParam(name = "redirectURL", required = false) String redirectURL,
                            Model model) {
        model.addAttribute("memberLoginRequest", new MemberLoginRequest());
        model.addAttribute("redirectURL", redirectURL);
        return "member/login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute MemberLoginRequest request,
            BindingResult bindingResult,
            @RequestParam(required = false) String redirectURL,
            HttpServletRequest httpRequest,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "member/login";
        }

        try {
            MemberResponse member = memberService.login(request);

            // 세션 생성 및 로그인 정보 저장
            HttpSession session = httpRequest.getSession();
            session.setAttribute("loginMember", member);

            log.info("로그인 성공 - Username: {}, SessionId: {}",
                    member.getUsername(), session.getId());

            redirectAttributes.addFlashAttribute("message",
                    member.getName() + "님 환영합니다!");

            // 리다이렉트 URL이 있으면 해당 URL로, 없으면 홈으로
            if (redirectURL != null && !redirectURL.isEmpty()) {
                return "redirect:" + redirectURL;
            }
            return "redirect:/board/list";

        } catch (IllegalArgumentException e) {
            bindingResult.reject("loginFailed", e.getMessage());
            return "member/login";
        }
    }

    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); //로그아웃 : 세션 값 날리기
            log.info("로그아웃 완료");
        }

        redirectAttributes.addFlashAttribute("message", "로그아웃되었습니다.");
        return "redirect:/home";
    }

    /**
     * 마이페이지
     */
    @GetMapping("/mypage")
    public String mypage(
            HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        MemberResponse loginMember = (MemberResponse) session.getAttribute("loginMember");

        // 내 정보 조회
        MemberResponse member = memberService.getMember(loginMember.getId());

        // 내가 작성한 게시글 목록
        Page<BoardResponse> myBoards =
                boardService.getMyBoardList(loginMember.getId(), page, size);

        model.addAttribute("member", member);
        model.addAttribute("myBoards", myBoards);

        return "member/mypage";
    }

    /**
     * 회원 정보 수정 폼
     */
    @GetMapping("/edit")
    public String editForm(HttpSession session, Model model) {
        MemberResponse loginMember = (MemberResponse) session.getAttribute("loginMember");
        MemberResponse member = memberService.getMember(loginMember.getId());

        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .name(member.getName())
                .build();

        model.addAttribute("memberUpdateRequest", request);
        return "member/edit";
    }

    /**
     * 회원 정보 수정 처리
     */
    @PostMapping("/edit")
    public String edit(
            @Valid @ModelAttribute MemberUpdateRequest request,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "member/edit";
        }

        MemberResponse loginMember = (MemberResponse) session.getAttribute("loginMember");

        try {
            memberService.updateMember(loginMember.getId(), request);

            // 세션 정보 갱신
            MemberResponse updatedMember = memberService.getMember(loginMember.getId());
            session.setAttribute("loginMember", updatedMember);

            redirectAttributes.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
            return "redirect:/member/mypage";

        } catch (IllegalArgumentException e) {
            bindingResult.reject("updateFailed", e.getMessage());
            return "member/edit";
        }
    }

    /**
     * 회원 목록
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            Model model) {

        Page<MemberResponse> memberPage = memberService.getMemberList(page, size, searchType, keyword);

        int totalPages = memberPage.getTotalPages();
        long totalElements = memberPage.getTotalElements();
        int currentPage = memberPage.getNumber();

        int pageBlockSize = 10;
        int startPage = (currentPage / pageBlockSize) * pageBlockSize + 1;
        int endPage = Math.min(startPage + pageBlockSize - 1, totalPages);

        model.addAttribute("memberPage", memberPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);

        return "member/list";
    }

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("memberJoinRequest", new MemberJoinRequest());
        return "member/join";
    }

    @PostMapping("/join")
    public String join(
            @Valid @ModelAttribute MemberJoinRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        try {
            memberService.join(request);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/member/login";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("joinFailed", e.getMessage());
            return "member/join";
        }
    }
}
