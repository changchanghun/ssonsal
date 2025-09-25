package chworld.ssonsal.todo.repository;

import chworld.ssonsal.member.domain.Member;
import chworld.ssonsal.todo.domain.Todo;
import chworld.ssonsal.todo.domain.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByMemberAndStatusOrderByLevelAscIdDesc(Member member, TodoStatus todoStatus);
    List<Todo> findByMember(Member memberId);
    List<Todo> findByMemberIdAndStatus(Long memberId, TodoStatus status);
    @Query("SELECT t FROM Todo t WHERE t.member.id = :memberId ORDER BY t.uniqData ASC")
    List<Todo> findByMemberIdOrderByUniqDataAsc(@Param("memberId") Long memberId);
}
