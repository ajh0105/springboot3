package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
//비즈니스 로직: 서비스(비즈니스) 레이어 => 뷰 -> 컨트롤러 -> 서비스 -> 레포지터리 -> JPA(Entity) -> DB
//@Service(서비스) 단에서 MemberJoinRequest => Member Entity => save(Member member) => 회원가입
//MemberResponse <= Member Entity
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //자바 스프링 부트 3의 레포지터리 인터페이스에 정의된 메소드 규칙을 알려줘
    //회원 정보 찾기(마이페이지)
    //select * from member where username = ?;
    Optional<Member> findByUsername(String username);

    //아이디(username) 중복 확인
    //select count(*) from member where username = ?; => 1이상 true, 0이면 false
    boolean existsByUsername(String username);

    //관리자가 이름(name)으로 회원 정보 검색
    //select * from member where name is like "*"+keyword+"*";
    Page<Member> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    //관리자가 아이디(username)로 회원 정보 검색
    //select * from member where username is like "%"+keyword+"%";
    Page<Member> findByUsernameContainingIgnoreCase(String keyword, Pageable pageable);

    //관리자가 아이디(username) 또는 이름(name) 항목을 모두 검색
    @Query("SELECT m FROM Member m WHERE " +
            "LOWER(m.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Member> searchByUsernameOrName(@Param("keyword") String keyword, Pageable pageable);
}