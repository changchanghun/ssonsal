package chworld.ssonsal.member.service;

import chworld.ssonsal.member.domain.Member;
import chworld.ssonsal.member.domain.MemberStatus;
import chworld.ssonsal.member.domain.Role;
import chworld.ssonsal.member.dto.MemberRequest;
import chworld.ssonsal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isEmailDuplicate(String email) {
        return memberRepository.existsByMemberEmail(email);
    }

    public Member saveMember(MemberRequest request) {

        String encodedPassword = passwordEncoder.encode(request.getMemberPassword());
        Member member = new Member(
                request.getMemberEmail(),
                request.getMemberName(),
                encodedPassword,
                MemberStatus.DONE,
                Role.USER
        );
        return memberRepository.save(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}
