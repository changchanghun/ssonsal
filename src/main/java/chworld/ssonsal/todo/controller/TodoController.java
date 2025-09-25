package chworld.ssonsal.todo.controller;

import chworld.ssonsal.member.auth.CustomUserDetails;
import chworld.ssonsal.todo.domain.Todo;
import chworld.ssonsal.todo.dto.TodoRequest;
import chworld.ssonsal.todo.dto.TodoResponse;
import chworld.ssonsal.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @RequestBody @Valid TodoRequest todoRequest){
        todoRequest.setMemberId(userDetails.getId());
        Todo created = todoService.createTodos(todoRequest);
        return ResponseEntity.ok(new TodoResponse(created));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<Map<String, List<TodoResponse>>> getTodosByStatus(@PathVariable Long memberId){
        Map<String, List<TodoResponse>> todos = todoService.getTodosByMemberGroupedByStatus(memberId);
        return ResponseEntity.ok(todos);
    }

    @PostMapping("/{todoId}/complete")
    public ResponseEntity<TodoResponse> completeTodo(@PathVariable Long todoId,
                                                     @RequestParam String sessionId){

        Todo completed = todoService.completeTodo(todoId, sessionId);
        return ResponseEntity.ok(new TodoResponse(completed));
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId){
        todoService.deleteTodo(todoId);
        return ResponseEntity.noContent().build();
    }

}
