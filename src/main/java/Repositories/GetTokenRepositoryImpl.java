package Repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

interface GetTokenRepository {
    // randomの最大値を取得する
    public abstract Double getMax(CollectionReference testNotification);

    // 取得した最大値を使ってドキュメントをランダムに取得する
    public  abstract List<String> getDocumentAtRandom(CollectionReference testNotification, Double max);
}

public class GetTokenRepositoryImpl implements GetTokenRepository {

    // testNotificationコレクションから送り先のトークンを取得
    public List<String> getToken() throws InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference testNotification = db.collection("testNotification");

        GetTokenRepositoryImpl getTokenRepositoryImpl = new GetTokenRepositoryImpl();
        Double max = getTokenRepositoryImpl.getMax(testNotification);
        return getDocumentAtRandom(testNotification, max);

    }

    // FIXME: get()使うとexception投げないとダメみたいやけどオーバーライドしたメソッドはexceptioon投げれない
    @Override
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

    @Override
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

            for (DocumentSnapshot document : documents) {
                token = document.getString("token");
                uid = document.getString("uid");
            }

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
