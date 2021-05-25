package Repositories;

import Entities.GetTokenRepositoryEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetTokenRepositoryImpl implements GetTokenRepository {

    public GetTokenRepositoryEntity getToken() throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference testNotification = db.collection("testNotification");

        Double max = getMax(testNotification);
        return getRandomUserToken(testNotification, max);
    }

    public Double getMax(CollectionReference testNotification) throws InterruptedException, ExecutionException {
        Query query_max = testNotification
                .orderBy("random", Query.Direction.DESCENDING)
                .limit(1);
        ApiFuture<QuerySnapshot> querySnapshotMax = query_max.get();

        Double max = null;

        if (querySnapshotMax.get().getDocuments().iterator().hasNext()) {
            max = querySnapshotMax.get().getDocuments().iterator().next().getDouble("random");
        }

        return max;
    }

    public GetTokenRepositoryEntity getRandomUserToken(CollectionReference testNotification, Double max) throws InterruptedException, ExecutionException {

        String token = null;
        String uid = null;

        Query query = testNotification
                .whereGreaterThanOrEqualTo("random", Math.random() * max)
                .orderBy("random", Query.Direction.ASCENDING)
                .limit(1);

        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = null;

        documents = future.get().getDocuments();

        if (documents.iterator().hasNext()) {
            var document = documents.iterator().next();
            token = document.getString("token");
            uid = document.getString("uid");
        }

        GetTokenRepositoryEntity helper = new GetTokenRepositoryEntity();
        helper.setToken(token);
        helper.setUid(uid);
        return helper;
    }
}
