package chworld.ssonsal.web;

import chworld.ssonsal.member.auth.CustomUserDetails;
import chworld.ssonsal.session.domain.SessionStatus;
import chworld.ssonsal.todo.domain.Todo;
import chworld.ssonsal.todo.domain.TodoStatus;
import chworld.ssonsal.todo.dto.TodoResponse;
import chworld.ssonsal.todo.repository.TodoRepository;
import chworld.ssonsal.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final TodoRepository todoRepository;

    @Autowired
    private TodoService todoService;

    @GetMapping("/register")
    public String register(){ return "register"; }

    @GetMapping("/find_id")
    public String find_id(){ return "find_id"; }

    @GetMapping("/find_pw")
    public String find_pw(){ return "find_pw"; }

    @GetMapping("/find_res")
    public String find_res(@RequestParam("type") String type, RedirectAttributes redirect){
        if(!("1".equals(type) || "2".equals(type))){
                return "redirect:/";
        }

        return "find_res";
    }

    @GetMapping("/login")
    public String login(){ return "login"; }

    @GetMapping("/log")
    public String log(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){
        Long memberId = userDetails.getId();
        Map<LocalDate, Map<String, List<Todo>>> groupedTodos = todoService.getGroupedTodos(memberId);

        List<Todo> allTodos = groupedTodos.values().stream()
                .flatMap(map -> map.values().stream())
                .flatMap(List::stream)
                .toList();

        Map<String, TodoService.SessionTimeInfo> sessionTimeMap = todoService.getSessionTimeInfoMap(allTodos);

        model.addAttribute("groupedTodos", groupedTodos);
        model.addAttribute("sessionTimeMap", sessionTimeMap);

        return "log";
    }

    @GetMapping("/")
    public String main(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if(userDetails == null){
            return "redirect:/login";
        }

        Long memberId = userDetails.getId();
        Map<String, List<TodoResponse>> todos = todoService.getTodosByMemberGroupedByStatus(memberId);
        model.addAttribute("todos", todos);
        return "index";
    }

    @GetMapping("/condition")
    public String condition() {
        return "condition";
    }

    @GetMapping("/trip")
    public String trip(@AuthenticationPrincipal CustomUserDetails userDetails,
                       @RequestParam(name="condition", required=false) SessionStatus condition,
                       Model model) {
        Long memberId = userDetails.getId();

        List<TodoResponse> sortedTodos = todoService.getTodosByMemberSortedByCondition(memberId,condition);

        model.addAttribute("todos", sortedTodos);
        model.addAttribute("condition",condition);

        return "trip";
    }

    @GetMapping("/trip-end")
    public String tripEnd() {
        return "trip-end";
    }

}