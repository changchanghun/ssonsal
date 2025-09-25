package chworld.ssonsal.member.domain;

import chworld.ssonsal.todo.domain.Todo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="member_TBL")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="m_email", nullable=false, unique = true)
    private String memberEmail;

    @Column(name="m_name", nullable = false)
    private String memberName;

    // 비밀번호 해시
    @Column(name="m_pw", nullable = false)
    private String memberPassword;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Todo> todos = new ArrayList<>();

    public Member(String memberEmail, String memberName, String memberPassword, MemberStatus status, chworld.ssonsal.member.domain.Role user){
        this.memberEmail = memberEmail;
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.createdDate = LocalDateTime.now();
        this.status = MemberStatus.DONE;
        this.role = Role.USER;
    }
}
