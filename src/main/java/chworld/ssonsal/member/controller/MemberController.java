package chworld.ssonsal.member.controller;

import chworld.ssonsal.member.dto.MemberRequest;
import chworld.ssonsal.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
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
        memberService.saveMember(memberRequest);
        return ResponseEntity.ok("회원가입 완료");
    }

}
