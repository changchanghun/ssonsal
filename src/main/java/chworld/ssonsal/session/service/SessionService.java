package chworld.ssonsal.session.service;

import chworld.ssonsal.member.domain.Member;
import chworld.ssonsal.session.domain.Session;
import chworld.ssonsal.session.dto.SessionRequest;
import chworld.ssonsal.member.repository.MemberRepository;
import chworld.ssonsal.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final MemberRepository memberRepository;

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
    public Session endSession(Long sessionId, int time){
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(()->new IllegalArgumentException("세션을 찾을 수 없습니다."));

        Instant instantKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toInstant();
        session.setEndTime(instantKST);
        session.setTime(time);

        return sessionRepository.save(session);
    }

}
