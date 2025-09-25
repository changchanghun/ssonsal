package chworld.ssonsal.todo.dto;

import chworld.ssonsal.todo.domain.Todo;
import chworld.ssonsal.todo.domain.TodoLevel;
import chworld.ssonsal.todo.domain.TodoStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
public class TodoResponse {
    private Long id;
    private String title;
    private TodoLevel level;
    private TodoStatus status;
    private LocalDate planDtm;
    private String dDay;

    public TodoResponse(Todo todo){
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.level = todo.getLevel();
        this.status = todo.getStatus();
        this.planDtm = todo.getPlanDtm();
        this.dDay = calculateDDay(todo.getPlanDtm());
    }

    private String calculateDDay(LocalDate planDtm) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), planDtm);
        if (days > 0) return "D-" + days;
        else if (days == 0) return "D-day";
        else return "D+" + Math.abs(days);
    }
}
