package com.example.demo.dto;

import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글 작성/수정 요청 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequest {

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    @Size(min = 2, max = 200, message = "제목은 2자 이상 200자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    @Size(min = 10, message = "내용은 10자 이상 입력해주세요.")
    private String content;

    /**
     * Entity 변환
     */
    public Board toEntity(Member member) {
        return Board.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }
}