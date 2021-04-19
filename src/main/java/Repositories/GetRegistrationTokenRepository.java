package Repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetRegistrationTokenRepository {

    // testNotificationコレクションから送り先のトークンを取得
    public static List<String> getToken() throws IOException, ExecutionException, InterruptedException {
        String token = null;
        String uid = null;
        List<String> resultList = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();

        // ============= ランダムにドキュメントを取得する ================
        CollectionReference testNotification = db.collection("testNotification");

        // -----------maxを用意するコード(後でメソッド化)-----------
        Query query_max = testNotification
                .orderBy("random", Query.Direction.DESCENDING)
                .limit(1);
       ApiFuture<QuerySnapshot> querySnapshotMax = query_max.get();

       Double max = null;
       for (DocumentSnapshot document : querySnapshotMax.get().getDocuments()) {
           max = document.getDouble("random");
       }
       // -----------------------------------------------------

        // -----------ランダムにドキュメントを取得する本番コード-----------
        // FIXME: randomから毎回最大値のみ取得されている気がする。
        //  本当にランダムにrandomが選択されているか、randomを表示させて確認する
        Query query = testNotification
                .whereGreaterThanOrEqualTo("random", Math.random()*max)
                .orderBy("random", Query.Direction.ASCENDING)
                .limit(1);

        ApiFuture<QuerySnapshot> future = query.get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (DocumentSnapshot document : documents) {
            token = document.getString("token");
            uid = document.getString("uid");
        }

        resultList.add(0, token);
        resultList.add(1, uid);

        return resultList;
    }
}
