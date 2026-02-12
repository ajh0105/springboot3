package com.example.demo.service;


import com.example.demo.dto.BoardRequest;
import com.example.demo.dto.BoardResponse;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글 목록 조회 (페이징, 검색)
     */
    public Page<BoardResponse> getBoardList(int page, int size, String searchType, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Board> boardPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            switch (searchType) {
                case "title":
                    boardPage = boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);
                    break;
                case "content":
                    boardPage = boardRepository.findByContentContainingIgnoreCase(keyword, pageable);
                    break;
                case "writer":
                    boardPage = boardRepository.findByMemberNameContaining(keyword, pageable);
                    break;
                case "all":
                    boardPage = boardRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
                    break;
                default:
                    boardPage = boardRepository.findAll(pageable);
            }
        } else {
            boardPage = boardRepository.findAll(pageable);
        }

        return boardPage.map(BoardResponse::fromEntity);
    }

    /**
     * 게시글 상세 조회 (조회수 증가)
     */
    @Transactional
    public BoardResponse getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        board.incrementViews();

        log.info("게시글 조회 - ID: {}, 조회수: {}", board.getId(), board.getViews());

        return BoardResponse.fromEntity(board);
    }

    /**
     * 게시글 조회 (조회수 증가 없음 - 수정 시 사용)
     */
    public BoardResponse getBoardForEdit(Long id, Long memberId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 작성자 확인
        if (!board.isWriter(memberId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        return BoardResponse.fromEntity(board);
    }

    /**
     * 게시글 작성
     */
    @Transactional
    public Long createBoard(BoardRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Board board = request.toEntity(member);
        Board savedBoard = boardRepository.save(board);

        log.info("게시글 작성 완료 - ID: {}, 작성자: {}", savedBoard.getId(), member.getName());

        return savedBoard.getId();
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public void updateBoard(Long id, BoardRequest request, Long memberId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 작성자 확인
        if (!board.isWriter(memberId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        board.updateBoard(request.getTitle(), request.getContent());

        log.info("게시글 수정 완료 - ID: {}", id);
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deleteBoard(Long id, Long memberId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 작성자 확인
        if (!board.isWriter(memberId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        boardRepository.delete(board);

        log.info("게시글 삭제 완료 - ID: {}", id);
    }

    /**
     * 내가 작성한 게시글 목록
     */
    public Page<BoardResponse> getMyBoardList(Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Board> boardPage = boardRepository.findByMemberId(memberId, pageable);

        return boardPage.map(BoardResponse::fromEntity);
    }
}