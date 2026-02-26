package com.example.tour.service;

import com.example.tour.domain.Member;
import com.example.tour.domain.MemberRole;
import com.example.tour.dto.MemberFormDto;
import com.example.tour.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    public Long save(MemberFormDto dto) {

        // 아이디 중복 체크
        memberRepository.findByUsername(dto.getUsername())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다.");
                });

        Member member = Member.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword())) //암호화
                .name(dto.getName())
                .email(dto.getEmail())
                .role(MemberRole.USER)
                .build();

        return memberRepository.save(member).getId();
    }

    //회원 목록
    @Transactional
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    //id(회원번호)에 의한 특정 회원 검색
    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    // 로그인 검증
    @Transactional
    public Member login(String username, String password) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("아이디 없음"));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }

        return member;
    }

    public boolean existsByUsername(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }
}
