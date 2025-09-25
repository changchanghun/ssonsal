package chworld.ssonsal.email.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="email_verificationTBL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="m_email", nullable=false)
    private String email;

    @Column(name="code", nullable=false)
    private String code;

    private LocalDateTime createdAt;

    public EmailVerification(String email, String code){
        this.email = email;
        this.code = code;
        this.createdAt = LocalDateTime.now();
    }


}
