//package temaatica.controllers;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import temaatica.models.Task;
//import temaatica.services.TaskService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class TaskController {
//
//    private final TaskService taskService;
//
//    @GetMapping("/tasks")
//    public ResponseEntity<List<Task>> getTasks() {
//        try {
//            List<Task> tasks = taskService.getTasks();
//            return ResponseEntity.ok(tasks);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//}
//
