package chworld.ssonsal.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {

    private String memberEmail;
    private String memberName;
    private String memberPassword;
    private String phoneNumber;
    private String status;

}
