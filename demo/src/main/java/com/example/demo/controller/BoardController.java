package com.example.demo.controller;


import com.example.demo.dto.BoardRequest;
import com.example.demo.dto.BoardResponse;
import com.example.demo.dto.MemberResponse;
import com.example.demo.service.BoardService;
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

/**
 * 게시판 컨트롤러
 */
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 목록
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            Model model) {

        Page<BoardResponse> boardPage = boardService.getBoardList(page, size, searchType, keyword);

        int totalPages = boardPage.getTotalPages();
        long totalElements = boardPage.getTotalElements();
        int currentPage = boardPage.getNumber();

        int pageBlockSize = 10;
        int startPage = (currentPage / pageBlockSize) * pageBlockSize + 1;
        int endPage = Math.min(startPage + pageBlockSize - 1, totalPages);

        model.addAttribute("boardPage", boardPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);

        return "board/list";
    }

    /**
     * 게시글 상세
     */
    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        BoardResponse board = boardService.getBoard(id);

        // 로그인한 사용자인지 확인
        MemberResponse loginMember = (MemberResponse) session.getAttribute("loginMember");
        boolean isWriter = false;
        if (loginMember != null) {
            isWriter = board.getWriterId().equals(loginMember.getId());
        }

        model.addAttribute("board", board);
        model.addAttribute("isWriter", isWriter);

        return "board/detail";
    }

    /**
     * 게시글 작성 폼
     */
    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("boardRequest", new BoardRequest());
        return "board/write";
    }

    /**
     * 게시글 작성 처리
     */
    @PostMapping("/write")
    public String write(
            @Valid @ModelAttribute BoardRequest request,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "board/write";
        }

        MemberResponse loginMember = (MemberResponse) session.getAttribute("loginMember");

        try {
            Long boardId = boardService.createBoard(request, loginMember.getId());
            redirectAttributes.addFlashAttribute("message", "게시글이 등록되었습니다.");
            return "redirect:/board/detail/" + boardId;
        } catch (Exception e) {
            log.error("게시글 작성 실패", e);
            bindingResult.reject("writeFailed", "게시글 등록에 실패했습니다.");
            return "board/write";
        }
    }

    /**
     * 게시글 수정 폼
     */
    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        MemberResponse loginMember = (MemberResponse) session.getAttribute("loginMember");

        try {
            BoardResponse board = boardService.getBoardForEdit(id, loginMember.getId());

            BoardRequest request = BoardRequest.builder()
                    .title(board.getTitle())
                    .content(board.getContent())
                    .build();

            model.addAttribute("boardRequest", request);
            model.addAttribute("boardId", id);

            return "board/edit";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/board/detail/" + id;
        }
    }

    /**
     * 게시글 수정 처리
     */
    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @Valid @ModelAttribute BoardRequest request,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("boardId", id);
            return "board/edit";
        }

        MemberResponse loginMember = (MemberResponse) session.getAttribute("loginMember");

        try {
            boardService.updateBoard(id, request, loginMember.getId());
            redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
            return "redirect:/board/detail/" + id;

        } catch (IllegalArgumentException e) {
            bindingResult.reject("updateFailed", e.getMessage());
            model.addAttribute("boardId", id);
            return "board/edit";
        }
    }

    /**
     * 게시글 삭제
     */
    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        MemberResponse loginMember = (MemberResponse) session.getAttribute("loginMember");

        try {
            boardService.deleteBoard(id, loginMember.getId());
            redirectAttributes.addFlashAttribute("message", "게시글이 삭제되었습니다.");
            return "redirect:/board/list";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/board/detail/" + id;
        }
    }
}
