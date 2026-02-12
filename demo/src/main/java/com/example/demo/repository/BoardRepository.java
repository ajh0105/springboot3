package com.example.demo.repository;

import com.example.demo.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    //save(), findById(), findAll(), deleteById(), count()
    //제목으로 검색하여 페이징
    //select * from board where title like '%' + keyword + '%';
    Page<Board> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    //내용으로 검색하여 페이징
    //select * from board where content like '%' + keyword + '%';
    Page<Board> findByContentContainingIgnoreCase(String keyword, Pageable pageable);

    //작성자 이름으로 검색하여 페이징
    @Query("SELECT b FROM Board b WHERE LOWER(b.member.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Board> findByMemberNameContaining(@Param("keyword") String keyword, Pageable pageable);

    Page<Board> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);

    //회원이 쓴 글 조회
    Page<Board> findByMemberId(Long memberId, Pageable pageable);

    //회원별 게시글 수 조회
    long countByMemberId(Long memberId);
}