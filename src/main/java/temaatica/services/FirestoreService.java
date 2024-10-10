package temaatica.services;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.stereotype.Service;
import temaatica.repositories.FirestoreRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreService {

    private final FirestoreRepository firestoreRepository;

    public FirestoreService(FirestoreRepository firestoreRepository) {
        this.firestoreRepository = firestoreRepository;
    }

    public List<QueryDocumentSnapshot> getAllDocuments(String collectionName) throws ExecutionException, InterruptedException {
        return firestoreRepository.getAllDocuments(collectionName);
    }

    public DocumentSnapshot getDocument(String collectionName, String documentId) throws ExecutionException, InterruptedException {
        return firestoreRepository.getDocument(collectionName, documentId);
    }

    public void createDocument(String collectionName, Map<String, Object> data) {
        firestoreRepository.createDocument(collectionName, data);
    }

    public void updateDocument(String collectionName, String documentId, Map<String, Object> data) {
        firestoreRepository.updateDocument(collectionName, documentId, data);
    }

    public void deleteDocument(String collectionName, String documentId) {
        firestoreRepository.deleteDocument(collectionName, documentId);
    }
}