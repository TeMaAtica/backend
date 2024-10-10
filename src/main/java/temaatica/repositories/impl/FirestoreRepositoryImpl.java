package temaatica.repositories.impl;

import com.google.cloud.firestore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import temaatica.repositories.FirestoreRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
public class FirestoreRepositoryImpl implements FirestoreRepository {

    private final Firestore firestore;

    public FirestoreRepositoryImpl(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public List<QueryDocumentSnapshot> getAllDocuments(String collectionName) throws InterruptedException, ExecutionException {
        Query query = firestore.collection(collectionName);
        QuerySnapshot querySnapshot = query.get().get();
        return querySnapshot.getDocuments();
    }

    @Override
    public DocumentSnapshot getDocument(String collectionName, String documentId) throws InterruptedException, ExecutionException {
        return firestore.collection(collectionName).document(documentId).get().get();
    }

    @Override
    public void createDocument(String collectionName, Map<String, Object> data) {
        firestore.collection(collectionName).add(data);
    }

    @Override
    public void createOrUpdateDocument(String collectionName, String documentId, Object data) {
        firestore.collection(collectionName).document(documentId).set(data);
    }

    @Override
    public void updateDocument(String collectionName, String documentId, Map<String, Object> data) {
        DocumentReference docRef = firestore.collection(collectionName).document(documentId);
        docRef.set(data, SetOptions.merge());
    }

    @Override
    public void deleteDocument(String collectionName, String documentId) {
        firestore.collection(collectionName).document(documentId).delete();
    }

    @Override
    public QueryDocumentSnapshot findDocumentByField(String collectionName, String fieldName, String value) throws InterruptedException, ExecutionException {
        CollectionReference collection = firestore.collection(collectionName);
        Query query = collection.whereEqualTo(fieldName, value);
        QuerySnapshot querySnapshot = query.get().get();

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        if (!documents.isEmpty()) {
            return documents.get(0);
        }
        return null;
    }
}
