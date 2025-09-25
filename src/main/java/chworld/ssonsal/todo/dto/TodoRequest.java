package chworld.ssonsal.todo.dto;

import chworld.ssonsal.todo.domain.Todo;
import chworld.ssonsal.todo.domain.TodoLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequest {

    private Long memberId;

    @NotBlank
    private String title;

    @NotNull
    private TodoLevel level;

    @NotNull
    private LocalDate planDtm;

    private String uniqData;
    private int perceivedTime;

}
