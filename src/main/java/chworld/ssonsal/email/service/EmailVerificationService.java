package chworld.ssonsal.email.service;

import chworld.ssonsal.email.domain.EmailVerification;
import chworld.ssonsal.email.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationRepository repository;
    private final MailService mailService;

    @Transactional
    public void sendCode(String email){
        String code = generateCode();
        repository.deleteByEmail(email);
        repository.save(new EmailVerification(email, code));
        mailService.sendVerificationMail(email,code);
    }

    public boolean verifyCode(String email, String inputCode){
        return repository.findByEmail(email)
                .filter(v-> v.getCode().equals(inputCode))
                .filter(v->v.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(3)))
                .isPresent();
    }

    private String generateCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }
}
