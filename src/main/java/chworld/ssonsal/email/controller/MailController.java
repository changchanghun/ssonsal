package chworld.ssonsal.email.controller;

import chworld.ssonsal.email.dto.EmailRequest;
import chworld.ssonsal.email.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class MailController {
    private final EmailVerificationService verificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationCode(@RequestBody EmailRequest request) {
        String email = request.getEmail().trim();
        verificationService.sendCode(email);
        return ResponseEntity.ok("인증번호가 전송되었습니다.");
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyCode(@RequestBody EmailRequest request) {
        boolean isValid = verificationService.verifyCode(request.getEmail().trim(), request.getCode().trim());
        return ResponseEntity.ok(isValid);
    }
}
