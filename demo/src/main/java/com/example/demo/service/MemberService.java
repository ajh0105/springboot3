package com.example.demo.service;

import com.example.demo.dto.MemberJoinRequest;
import com.example.demo.dto.MemberLoginRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.dto.MemberUpdateRequest;
import com.example.demo.entity.Member;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    //MemberJoinRequest => Member
    @Transactional
    public Long join(MemberJoinRequest request) {
        //아이디 중복 확인
        if(memberRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        //비밀번호 일치 확인
        if(!request.isPasswordMatch()) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Member member = request.toEntity(); //MemberJoinRequest => Member
        Member savedMember = memberRepository.save(member);

        log.info("회원가입 완료 - ID: {}, Username: {}", savedMember.getId(), savedMember.getUsername());
        return savedMember.getId();
    }

    //로그인
    public MemberResponse login(MemberLoginRequest request) {
        //아이디가 일치하는 회원 불러오기
        Member member = memberRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.")
        );

        if(!member.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        log.info("로그인 성공 - Username: {}", member.getUsername());
        //member => memberResponse
        return MemberResponse.fromEntity(member);
    }

    //관리자 회원 목록 조회  getMemberList(1, 10, "username", "kim");
    public Page<MemberResponse> getMemberList(int page, int size, String searchType, String keyword) {
        Page<Member> memberPage;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if(keyword != null && !keyword.trim().isEmpty()) {
            switch (searchType) {
                case "username":
                    memberPage = memberRepository.findByUsernameContainingIgnoreCase(keyword, pageable);
                    break;
                case "name":
                    memberPage = memberRepository.findByNameContainingIgnoreCase(keyword, pageable);
                    break;
                case "all":
                    memberPage = memberRepository.searchByUsernameOrName(keyword, pageable);
                    break;
                default:
                    memberPage = memberRepository.findAll(pageable);
            }
        } else {
            memberPage = memberRepository.findAll(pageable);
        }
        
        //Member => MemberResponse : 게시글 수까지 포함된 데이터
        return memberPage.map(member -> {
            long boardCount = boardRepository.countByMemberId(member.getId());
            return MemberResponse.fromEntity(member, boardCount);
        });
    }

    /**
     * 회원 상세 조회
     */
    public MemberResponse getMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        long boardCount = boardRepository.countByMemberId(member.getId());

        return MemberResponse.fromEntity(member, boardCount);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public void updateMember(Long id, MemberUpdateRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 비밀번호 일치 확인
        if (!request.isPasswordMatch()) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        member.updateInfo(request.getPassword(), request.getName());

        log.info("회원 정보 수정 완료 - ID: {}", id);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        memberRepository.delete(member);

        log.info("회원 탈퇴 완료 - ID: {}, Username: {}", id, member.getUsername());
    }

    /**
     * 아이디 중복 확인
     */
    public boolean checkUsernameDuplicate(String username) {
        return memberRepository.existsByUsername(username);
    }
}