package chworld.ssonsal.session.dto;

import chworld.ssonsal.session.domain.Session;
import lombok.Getter;

import java.time.Instant;

@Getter
public class SessionResponse {
    private Long id;
    private Instant startTime;
    private Instant endTime;
    private String status;
    private int time;

    public SessionResponse(Session session) {
        this.id = session.getId();
        this.startTime = session.getStartTime();
        this.endTime = session.getEndTime();
        this.status = session.getStatus().name();
        this.time = session.getTime();
    }
}
