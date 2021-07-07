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

    public Pair getToken() throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference testNotification = db.collection("testNotification");

        Pair pair = getMax(testNotification);
        return getRandomUserToken(testNotification, pair);
    }

    public Pair getMax(CollectionReference testNotification) throws InterruptedException, ExecutionException {
        // testNotificationコレクションからドキュメントをランダムに一件取得
        Query query_max = testNotification
                .orderBy("random", Query.Direction.DESCENDING)
                .limit(1);
        ApiFuture<QuerySnapshot> querySnapshotMax = query_max.get();

        // querySnapshotMaxのバリデーションチェック
        // statusをSendLogicに返す
        Double max = null;
        ArrayList<String> errorCode = new ArrayList<>();
        if (querySnapshotMax == null) {
            // testNotificationコレクション内にドキュメントがなければ
            // エラーコードを取得
            ReturnErrorCodeEntity ec = new ReturnErrorCodeEntity();
            // errorCode配列にエラーコードを格納する
            errorCode.add(ec.ReturnErrorCode("cannot find document"));

            max = null;

            return new Pair(errorCode, max); // ドキュメントなし　max == null

        } else {
            // testNotificationコレクション内にドキュメントがあれば
            // randomの最大値を取得
            if (querySnapshotMax.get().getDocuments().iterator().hasNext()) {
                max = querySnapshotMax.get().getDocuments().iterator().next().getDouble("random");
            }
            errorCode.add("null");

            return new Pair(errorCode, max); // ドキュメントあり　max != null
        }
    }

    public Pair getRandomUserToken(CollectionReference testNotification, Pair pair) throws InterruptedException, ExecutionException {

        String token = null;
        String uid = null;

         // Pair pair からmaxを取り出す
        if(pair.right == null) {
            // getMax()でドキュメントがなかった場合
            // そのままPair<ErrorCode, null>の組み合わせを返す
            return pair;
        } else {
            // TODO: pairからmaxを受け取ってランダムにドキュメントを取得
            // FIXME: Objectになってしまう
            ArrayList<String> max = pair.right;

            Query query = testNotification
                    .whereGreaterThanOrEqualTo("random", Math.random() * (Double) max)
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

            // TODO: Pair pairのString[]にtokenとuidを入れる
            String tokenAndUid[] = new String[2];
            tokenAndUid[0] = token;
            tokenAndUid[1] = uid;

            // tokenとuidが入ったStringの配列とmaxを返す
            return new Pair<String[], String>(tokenAndUid, "has document");
        }
    }
}
