package chworld.ssonsal.member.service;

import chworld.ssonsal.email.service.MailService;
import chworld.ssonsal.member.domain.Member;
import chworld.ssonsal.member.domain.MemberStatus;
import chworld.ssonsal.member.domain.Role;
import chworld.ssonsal.member.dto.MemberRequest;
import chworld.ssonsal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    public boolean isEmailDuplicate(String email) {
        return memberRepository.existsByMemberEmail(email);
    }

    public boolean isPhoneDuplicate(String phone) {
        return memberRepository.existsByPhoneNumber(phone);
    }

    public Member saveMember(MemberRequest request) {

        String encodedPassword = passwordEncoder.encode(request.getMemberPassword());
        Member member = new Member(
                request.getMemberEmail(),
                request.getMemberName(),
                encodedPassword,
                request.getPhoneNumber(),
                MemberStatus.DONE,
                Role.USER
        );
        return memberRepository.save(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // 아이디 찾기
    public Optional<Member> findByMemberNameAndPhoneNumber(String name, String phone){
        return memberRepository.findByMemberNameAndPhoneNumber(name, phone);
    }

    public boolean sendResetPassword(String email, String phone){
      Optional<Member> memberOpt = memberRepository.findByMemberEmailAndPhoneNumber(email,phone);
        if (memberOpt.isEmpty()) return false;

        Member member = memberOpt.get();

        String tempPassword = generateTempPassword();
        mailService.sendTempPassword(email, tempPassword);

        String encodePassword = passwordEncoder.encode(tempPassword);
        member.setMemberPassword(encodePassword);
        memberRepository.save(member);
        return true;
    }

    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 10);
    };
}
