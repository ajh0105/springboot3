package com.example.demo.dto;

import com.example.demo.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 회원 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {

    private Long id;
    private String username;
    private String name;
    private LocalDateTime createdAt;
    private Long boardCount;  // 작성한 게시글 수

    /**
     * Entity(Member) -> DTO(MemberResponse) 변환
     */
    public static MemberResponse fromEntity(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .name(member.getName())
                .createdAt(member.getCreatedAt())
                .build();
    }

    /**
     * Entity -> DTO 변환 (게시글 수 포함)
     */
    public static MemberResponse fromEntity(Member member, Long boardCount) {
        return MemberResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .name(member.getName())
                .createdAt(member.getCreatedAt())
                .boardCount(boardCount)
                .build();
    }
}