package chworld.ssonsal.todo.service;

import chworld.ssonsal.member.domain.Member;
import chworld.ssonsal.session.domain.Session;
import chworld.ssonsal.session.domain.SessionStatus;
import chworld.ssonsal.session.repository.SessionRepository;
import chworld.ssonsal.todo.domain.Todo;
import chworld.ssonsal.todo.domain.TodoLevel;
import chworld.ssonsal.todo.domain.TodoStatus;
import chworld.ssonsal.todo.dto.TodoRequest;
import chworld.ssonsal.todo.dto.TodoResponse;
import chworld.ssonsal.member.repository.MemberRepository;
import chworld.ssonsal.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final SessionRepository sessionRepository;

    // 투두 생성
    public Todo createTodos(TodoRequest todoRequest){
        Member member = memberRepository.findById(todoRequest.getMemberId())
                .orElseThrow(()-> new IllegalArgumentException("회원 없음"));

        Todo todo = new Todo(
                member,
                todoRequest.getTitle(),
                todoRequest.getLevel(),
                todoRequest.getPlanDtm(),
                TodoStatus.PENDING
        );
        return todoRepository.save(todo);
    }

    // 투두 조회 (일반)
    public Map<String, List<TodoResponse>> getTodosByMemberGroupedByStatus(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음."));

        List<TodoResponse> doneTodos = todoRepository.findByMemberAndStatusOrderByLevelAscIdDesc(member, TodoStatus.DONE)
                .stream().map(TodoResponse::new).toList();

        List<TodoResponse> pendingTodos = todoRepository.findByMemberAndStatusOrderByLevelAscIdDesc(member, TodoStatus.PENDING)
                .stream().map(TodoResponse::new).toList();

        Map<String, List<TodoResponse>> result = new HashMap<>();
        result.put("DONE", doneTodos);
        result.put("PENDING", pendingTodos);

        return result;
    }

    // 투두 조회 (컨디션 별)
    public List<TodoResponse> getTodosByMemberSortedByCondition(Long memberId, SessionStatus sessionStatus){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalArgumentException("회원없음"));

        List<Todo> todos = todoRepository.findByMember(member);
        List<TodoLevel> priorityOrder = getPriorityOrderByCondition(sessionStatus);

        return todos.stream()
                .sorted(Comparator.comparingInt(todo->priorityOrder.indexOf(todo.getLevel())))
                .map(TodoResponse::new)
                .collect(Collectors.toList());
    }

    // 투두 완료
    public Todo completeTodo(Long todoId, String sessionId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("할 일을 찾을 수 없습니다."));

        todo.setStatus(TodoStatus.DONE);
        todo.setFinalDtm(LocalDate.now(ZoneId.of("Asia/Seoul")));
        todo.setUniqData(sessionId);

        return todoRepository.save(todo);
    }

    // 투두 삭제
    public void deleteTodo(Long todoId) {
        if (!todoRepository.existsById(todoId)) {
            throw new IllegalArgumentException("할 일을 찾을 수 없습니다.");
        }
        todoRepository.deleteById(todoId);
    }

    public List<TodoLevel> getPriorityOrderByCondition(SessionStatus sessionStatus){
        switch(sessionStatus){
            case BAD :
                return List.of(TodoLevel.LOW, TodoLevel.NORMAL, TodoLevel.HARD);
            case NORMAL:
                return List.of(TodoLevel.NORMAL, TodoLevel.LOW, TodoLevel.HARD);
            case GOOD:
                return List.of(TodoLevel.HARD, TodoLevel.NORMAL, TodoLevel.LOW);
            default:
                return List.of(TodoLevel.LOW, TodoLevel.NORMAL, TodoLevel.HARD);

        }
    }

    public Map<LocalDate, Map<String, List<Todo>>> getGroupedTodos(Long memberId) {
        List<Todo> todos = todoRepository.findByMemberIdAndStatus(memberId, TodoStatus.DONE);

        Map<LocalDate, Map<String, List<Todo>>> grouped = todos.stream()
                .filter(todo -> todo.getFinalDtm() != null && todo.getUniqData() != null)
                .collect(Collectors.groupingBy(
                        Todo::getFinalDtm,
                        () -> new TreeMap<>(Comparator.reverseOrder()), // 날짜 내림차순
                        Collectors.groupingBy(
                                Todo::getUniqData,
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                ));

        // 각 날짜 그룹 안의 리스트를 시간순으로 정렬
        for (Map<String, List<Todo>> innerMap : grouped.values()) {
            for (Map.Entry<String, List<Todo>> entry : innerMap.entrySet()) {
                List<Todo> sortedList = entry.getValue().stream()
                        .sorted(Comparator.comparing(Todo::getFinalDtm).reversed())
                        .collect(Collectors.toList());
                entry.setValue(sortedList);
            }
        }

        return grouped;
    }

    public Map<String, SessionTimeInfo> getSessionTimeInfoMap(List<Todo> todos) {
        Map<String, SessionTimeInfo> result = new LinkedHashMap<>();

        for (Todo todo : todos) {
            String uniqData = todo.getUniqData();
            if (uniqData == null || !uniqData.matches("\\d+")) continue;

            Long sessionId = Long.parseLong(uniqData);
            Optional<Session> sessionOpt = sessionRepository.findById(sessionId);

            if (sessionOpt.isPresent()) {
                Session session = sessionOpt.get();

                Duration duration = Duration.between(session.getStartTime(), session.getEndTime());
                long minutes = duration.toMinutes();
                long seconds = duration.minusMinutes(minutes).getSeconds();
                String realTime = String.format("%02d", minutes);

                int feelTime = (int) minutes + session.getTime(); // session.time은 분 단위

                result.put(uniqData, new SessionTimeInfo(realTime, feelTime));
            }
        }

        return result;
    }

    public class SessionTimeInfo {
        private String realTime;
        private int feelTime;

        public SessionTimeInfo(String realTime, int feelTime) {
            this.realTime = realTime;
            this.feelTime = feelTime;
        }

        public String getRealTime() {
            return realTime;
        }

        public int getFeelTime() {
            return feelTime;
        }
    }

}
