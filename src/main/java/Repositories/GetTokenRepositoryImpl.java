package Repositories;

import Entities.ReturnErrorCodeEntity;
import UseCases.Pair;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetTokenRepositoryImpl implements GetTokenRepository {

    public Pair<ArrayList<String>, String> getToken() throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference testNotification = db.collection("testNotification");

        Pair pair = getMax(testNotification);
        return getRandomUserToken(testNotification, pair);
    }

    public Pair<String, Double> getMax(CollectionReference testNotification) throws InterruptedException, ExecutionException {
        // testNotificationコレクションからドキュメントをランダムに一件取得
        Query query_max = testNotification
                .orderBy("random", Query.Direction.DESCENDING)
                .limit(1);
        ApiFuture<QuerySnapshot> querySnapshotMax = query_max.get();

        // querySnapshotMaxのバリデーションチェック
        // statusをSendLogicに返す
        Double max = null;
        if (querySnapshotMax == null) {
            // testNotificationコレクション内にドキュメントがなければ
            // エラーコードを取得
            ReturnErrorCodeEntity ec = new ReturnErrorCodeEntity();
            String errorCode = ec.ReturnErrorCode("cannot find document");

            max = null;

            return new Pair<String, Double>(errorCode, max); // ドキュメントなし　Pair(errorCode, null)

        } else {
            // testNotificationコレクション内にドキュメントがあれば
            // randomの最大値を取得
            if (querySnapshotMax.get().getDocuments().iterator().hasNext()) {
                max = querySnapshotMax.get().getDocuments().iterator().next().getDouble("random");
            }
            String errorCode = "null";

            return new Pair<String, Double>(errorCode, max); // ドキュメントあり　Pair("null", max)
        }
    }

    public Pair<ArrayList<String>, String> getRandomUserToken(CollectionReference testNotification, Pair pair) throws InterruptedException, ExecutionException {

        String token = null;
        String uid = null;

         // Pair pair からmaxを取り出す
        if(pair.right == null) {
            // getMax()でドキュメントがなかった場合
            return pair; // Pair(errorCode, null)

        } else {
            // pairからmaxを受け取ってランダムにドキュメントを取得
            Object max = pair.right;

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

            // Pair pairのString[]にtokenとuidを入れる
            ArrayList<String> tokenAndUid = new ArrayList<>();
            tokenAndUid.add(token);
            tokenAndUid.add(uid);

            // tokenとuidが入ったStringの配列とmaxを返す
            return new Pair<ArrayList<String>, String>(tokenAndUid, "has document");
        }
    }
}
