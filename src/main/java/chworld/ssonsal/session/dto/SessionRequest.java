package chworld.ssonsal.session.dto;

import chworld.ssonsal.session.domain.SessionStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SessionRequest {
    private Long memberId;
    private SessionStatus status;
    private int time;
    private String finalTime;
}
