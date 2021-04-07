package Repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetRegistrationTokenRepository {

    // testNotificationコレクションから送り先のトークンを取得
    public static List<String> getToken(HttpResponse response) throws IOException, ExecutionException, InterruptedException {
        BufferedWriter writer = response.getWriter();
        writer.write("in GetRegistrationTokenRepository\n");
        String token = null;
        String uid = null;
        List<String> resultList = null;

        Firestore db = FirestoreClient.getFirestore();

        // ランダムにドキュメントを取得する
        CollectionReference testNotification = db.collection("testNotification");
        writer.write("in testNotification collection\n");
        Query query = testNotification
                .whereGreaterThanOrEqualTo("random", Math.random())
                .orderBy("random", Query.Direction.ASCENDING)
                .limit(1);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        writer.write("after send query\n");

        // TODO: querySnapshotを出力する


        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            writer.write("in for loop\n");
            writer.write("token" + document.getString(token) + "\n");
            writer.write("uid" + document.getString(uid) + "\n");
            token = document.getString("token");
            uid = document.getString("uid");
        }
        writer.write("after for loop\n");

        resultList.set(0, token);
        resultList.set(1, uid);

        writer.write(token);
        writer.write(uid);

        return resultList;
    }
}
