package com.example.demo.dto;

import com.example.demo.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 게시글 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private String writerName;
    private Long writerId;
    private Integer views;
    private LocalDateTime createdAt;

    /**
     * Entity -> DTO 변환
     */
    public static BoardResponse fromEntity(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writerName(board.getMember().getName())
                .writerId(board.getMember().getId())
                .views(board.getViews())
                .createdAt(board.getCreatedAt())
                .build();
    }
}