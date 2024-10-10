//package temaatica.services;
//
//import com.google.cloud.firestore.QueryDocumentSnapshot;
//import lombok.RequiredArgsConstructor;
////import org.springframework.scheduling.config.Task;
//import org.springframework.stereotype.Service;
//import temaatica.repositories.FirestoreRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
//@Service
//@RequiredArgsConstructor
//public class TaskService {
//
//    private final FirestoreRepository firestoreRepository;
//
//    public List<Task> getTasks() throws InterruptedException, ExecutionException {
//        List<QueryDocumentSnapshot> documents = firestoreRepository.getAllDocuments("tasks");
//        List<Task> tasks = new ArrayList<>();
//        for (QueryDocumentSnapshot doc : documents) {
//            Task task = doc.toObject(Task.class);
//            task.setId(doc.getId());
//            tasks.add(task);
//        }
//        return tasks;
//    }
//}