package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) //지연 로딩
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;      //member.id => member_id

    @Column(nullable = false, columnDefinition = "integer default 0")
    @Builder.Default
    private Integer views = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //게시글 수정
    public void updateBoard(String title, String content) {
        if(title != null && !title.isEmpty()) {
            this.title = title;
        }
        if(content != null && !content.isEmpty()) {
            this.content = content;
        }
    }

    //작성자 확인
    public boolean isWriter(Long memberId) {
        return this.member.getId().equals(memberId);
    }

    public void incrementViews() {
        this.views++;
    }
}
//id, title, content, member(id, username, passsword, name), createdAt, views