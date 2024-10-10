package temaatica.repositories;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface FirestoreRepository {

    List<QueryDocumentSnapshot> getAllDocuments(String collectionName) throws InterruptedException, ExecutionException;

    DocumentSnapshot getDocument(String collectionName, String documentId) throws InterruptedException, ExecutionException;

    void createDocument(String collectionName, Map<String, Object> data);

    void createOrUpdateDocument(String collectionName, String documentId, Object data);

    void updateDocument(String collectionName, String documentId, Map<String, Object> data);

    void deleteDocument(String collectionName, String documentId);

    QueryDocumentSnapshot findDocumentByField(String collectionName, String fieldName, String value) throws InterruptedException, ExecutionException;
}