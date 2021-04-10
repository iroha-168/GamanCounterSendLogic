package Repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetRegistrationTokenRepository {

    // testNotificationコレクションから送り先のトークンを取得
    public static List<String> getToken(HttpResponse response) throws IOException, ExecutionException, InterruptedException {
        BufferedWriter writer = response.getWriter();
        writer.write("in GetRegistrationTokenRepository\n");
        String token = null;
        String uid = null;
        List<String> resultList = new ArrayList<>();
        QuerySnapshot documents = null;

        Firestore db = FirestoreClient.getFirestore();

        // ランダムにドキュメントを取得する
        CollectionReference testNotification = db.collection("testNotification");
        writer.write("in testNotification collection\n");

        // TODO: while文で処理が落ちてるのでちゃんと動くかplay groundとかで見直す
        while (documents == null) {
            writer.write("in while\n");
            Query query = testNotification
                    .whereGreaterThanOrEqualTo("random", Math.random())
                    .orderBy("random", Query.Direction.ASCENDING)
                    .limit(1);

            writer.write("made query\n");

            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            writer.write("querySnapshot: " + querySnapshot + "\n");

            documents = (QuerySnapshot) querySnapshot.get().getDocuments();
            writer.write("querySnapshot: " + documents + "\n");
        }

        writer.write("after send query\n");

        for (DocumentSnapshot document : documents) {
            writer.write("in for loop\n");
            writer.write("token: " + document.getString("token") + "\n");
            writer.write("uid: " + document.getString("uid") + "\n");
            token = document.getString("token");
            uid = document.getString("uid");
        }
        writer.write("after for loop\n");

        resultList.add(0, token);
        resultList.add(1, uid);

        writer.write("token: " + token + "\n");
        writer.write("uid: " + uid + "\n");

        return resultList;
    }
}
