package chworld.ssonsal.email.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmailRequest {
    private String email;
    private String code;
}
