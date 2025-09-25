package chworld.ssonsal.session.repository;

import chworld.ssonsal.member.domain.Member;
import chworld.ssonsal.session.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByMember(Member member);
}
