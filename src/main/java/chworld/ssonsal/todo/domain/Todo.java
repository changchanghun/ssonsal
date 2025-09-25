package chworld.ssonsal.todo.domain;

import chworld.ssonsal.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "todo_tbl")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoLevel level;

    @Column(nullable = false)
    private LocalDate planDtm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status;

    @Column(nullable = true)
    private LocalDate finalDtm;

    @Column(nullable = true)
    private String uniqData;

    public Todo(Member member, String title, TodoLevel level, LocalDate planDtm, TodoStatus status) {
        this.member = member;
        this.title = title;
        this.level = level;
        this.planDtm = planDtm;
        this.status = status;
    }

}
