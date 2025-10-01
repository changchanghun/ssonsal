package chworld.ssonsal.member.repository;

import chworld.ssonsal.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByMemberEmail(String memberEmail);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Member> findByMemberEmail(String memberEmail);
    Optional<Member> findByMemberNameAndPhoneNumber(String name, String phone);
    Optional<Member> findByMemberEmailAndPhoneNumber(String email, String phone);
}
