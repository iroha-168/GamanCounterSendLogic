package Repositories;

import UseCases.Pair;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetTokenRepositoryImpl implements GetTokenRepository {

    public Pair<ArrayList<String>, Boolean> getToken() throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference testNotification = db.collection("testNotification");

        Double max = getMax(testNotification);
        return getRandomUserToken(testNotification, max);
    }

    public Double getMax(CollectionReference testNotification) throws InterruptedException, ExecutionException {
        // testNotificationコレクションからドキュメントをランダムに一件取得
        Query query_max = testNotification
                .orderBy("random", Query.Direction.DESCENDING)
                .limit(1);
        ApiFuture<QuerySnapshot> querySnapshotMax = query_max.get();

        // randomの最大値を取得
        Double max = null;
        if (querySnapshotMax.get().getDocuments().iterator().hasNext()) {
            max = querySnapshotMax.get().getDocuments().iterator().next().getDouble("random");
        }
        return max;
    }

    public Pair<ArrayList<String>, Boolean> getRandomUserToken(CollectionReference testNotification, Double max) throws InterruptedException, ExecutionException {

        Boolean haveDocument = true;
        ArrayList<String> tokenAndUid = null;
        String token = null;
        String uid = null;

        // maxに値が入っていない場合=testNotificationドキュメントにコレクションが無かった場合
        if (max == null) {
            haveDocument = false;
            return new Pair<ArrayList<String>, Boolean>(tokenAndUid, haveDocument);
        }

        // maxに値が入っている場合=testNotificationドキュメントにコレクションが存在した場合
        Query query = testNotification
                .whereGreaterThanOrEqualTo("random", Math.random() * (Double) max)
                .orderBy("random", Query.Direction.ASCENDING)
                .limit(1);

        ApiFuture<QuerySnapshot> future = query.get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if (documents.iterator().hasNext()) {
            var document = documents.iterator().next();
            token = document.getString("token");
            uid = document.getString("uid");
        }
        tokenAndUid.add(0, token);
        tokenAndUid.add(1, uid);

        return new Pair<ArrayList<String>, Boolean>(tokenAndUid, haveDocument);
    }
}
