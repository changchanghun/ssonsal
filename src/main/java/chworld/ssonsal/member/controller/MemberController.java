package chworld.ssonsal.member.controller;

import chworld.ssonsal.member.domain.Member;
import chworld.ssonsal.member.dto.MemberRequest;
import chworld.ssonsal.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/check-email")
    @ResponseBody
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email){
        boolean isDuplicate = memberService.isEmailDuplicate(email);
        return ResponseEntity.ok(isDuplicate);
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> signup(@RequestBody MemberRequest memberRequest) {
        if (memberService.isEmailDuplicate(memberRequest.getMemberEmail())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 이메일입니다.");
        }

        if (memberService.isPhoneDuplicate(memberRequest.getPhoneNumber())){
            return ResponseEntity.badRequest().body("이미 사용 중인 번호입니다.");
        }

        memberService.saveMember(memberRequest);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/find-process")
    @ResponseBody
    public ResponseEntity<?> findId(@RequestBody MemberRequest memberRequest){
        Optional<Member> member = memberService.findByMemberNameAndPhoneNumber(memberRequest.getMemberName(),memberRequest.getPhoneNumber());
        if(member.isPresent()){
            return ResponseEntity.ok(member.get().getMemberEmail());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가입된 아이디가 없습니다.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody MemberRequest memberRequest){
        if(memberService.sendResetPassword(memberRequest.getMemberEmail(), memberRequest.getPhoneNumber())){
            return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일치하는 회원 정보를 찾을 수 없습니다.");
        }
    }

}
