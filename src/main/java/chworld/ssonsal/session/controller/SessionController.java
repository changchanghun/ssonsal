package chworld.ssonsal.session.controller;

import chworld.ssonsal.member.auth.CustomUserDetails;
import chworld.ssonsal.session.domain.Session;
import chworld.ssonsal.session.dto.SessionRequest;
import chworld.ssonsal.session.dto.SessionResponse;
import chworld.ssonsal.session.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/session")
public class SessionController {
    private final SessionService sessionService;

    // 세션 시작
    @PostMapping
    public ResponseEntity<SessionResponse> startSession(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestBody @Valid SessionRequest request){
        request.setMemberId(userDetails.getId());
        Session created = sessionService.createSession(request);
        return ResponseEntity.ok(new SessionResponse(created));
    }

    // 세션 종료
    @PatchMapping("/{sessionId}/end")
    public ResponseEntity<SessionResponse> endSession(@PathVariable Long sessionId,
                                                      @RequestBody @Valid SessionRequest request) {
        Session ended = sessionService.endSession(sessionId, request.getFinalTime() ,request.getTime());
        return ResponseEntity.ok(new SessionResponse(ended));
    }

}
