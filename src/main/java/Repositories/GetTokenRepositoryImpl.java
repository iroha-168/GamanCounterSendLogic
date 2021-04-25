package Repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetTokenRepositoryImpl implements GetTokenRepository {

    public List<String> getToken() {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference testNotification = db.collection("testNotification");

        GetTokenRepository getTokenRepository = new GetTokenRepositoryImpl();
        Double max = getTokenRepository.getMax(testNotification);
        return getDocumentAtRandom(testNotification, max);
    }

    public Double getMax(CollectionReference testNotification) {
        Query query_max = testNotification
                .orderBy("random", Query.Direction.DESCENDING)
                .limit(1);
        ApiFuture<QuerySnapshot> querySnapshotMax = query_max.get();

        Double max = null;
        try {
            if (querySnapshotMax.get().getDocuments().iterator().hasNext()) {
                max = querySnapshotMax.get().getDocuments().iterator().next().getDouble("random");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return max;
    }

    public List<String> getDocumentAtRandom(CollectionReference testNotification, Double max) {

        String token = null;
        String uid = null;
        List<String> resultList = new ArrayList<>();

        Query query = testNotification
                .whereGreaterThanOrEqualTo("random", Math.random()*max)
                .orderBy("random", Query.Direction.ASCENDING)
                .limit(1);

        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = null;

        try {
            documents = future.get().getDocuments();

            if (documents.iterator().hasNext()) {
                var document = documents.iterator().next();
                token = document.getString("token");
                uid = document.getString("uid");
            }

            resultList.add(0, token);
            resultList.add(1, uid);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
