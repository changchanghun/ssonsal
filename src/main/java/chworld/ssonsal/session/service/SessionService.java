package chworld.ssonsal.session.service;

import chworld.ssonsal.member.domain.Member;
import chworld.ssonsal.session.domain.Session;
import chworld.ssonsal.session.dto.SessionRequest;
import chworld.ssonsal.member.repository.MemberRepository;
import chworld.ssonsal.session.repository.SessionRepository;
import chworld.ssonsal.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;

    // 세션 생성
    public Session createSession(SessionRequest request){
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(()-> new IllegalArgumentException("회원 없음"));

        Session session = new Session(member, request.getStatus());
        return sessionRepository.save(session);
    }

    // 리스트
    public List<Session> getSessionByMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("회원 없음"));

        return sessionRepository.findByMember(member);
    }

    // 마지막 저장
    public Session endSession(Long sessionId, String finalTimeStr, int time){
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(()->new IllegalArgumentException("세션을 찾을 수 없습니다."));

        Instant baseTime = session.getStartTime();
        String[] parts = finalTimeStr.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        Duration duration = Duration.ofMinutes(minutes).plusSeconds(seconds);

        // 종료 시간 계산
        Instant calcuEndtime = baseTime.plus(duration);

        session.setEndTime(calcuEndtime);
        session.setTime(time);

        return sessionRepository.save(session);
    }

}
