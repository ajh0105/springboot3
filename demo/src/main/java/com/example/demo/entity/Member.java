package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 회원 엔티티
 */
@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;  //created_at

    //연관 관계
    //mappedBy: Board 엔티티의 member 필드와 매핑(id)
    //cascade: 회원 탈퇴 시 게시글도 같이 삭제
    //orphanRemoval: 글 삭제시 회원이 탈퇴되지 않게
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Board> boards = new ArrayList<>();

    //회원 정보 수정
    public void updateInfo(String password, String name) {
        if(password != null && !password.isEmpty()) {
            this.password = password;
        }
        if(name != null && !name.isEmpty()) {
            this.name = name;
        }
    }
}