package Repositories;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetRegistrationTokenRepository {

    // testNotificationコレクションから送り先のトークンを取得
    public static List<String> getToken() throws IOException, ExecutionException, InterruptedException {
        String projectId = "gamancounter-8546c";
        Double random = null;
        String token = null;
        String uid = null;
        List<String> resultList = null;

        // Cloud Firestore SDK 初期化
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build();
        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();

        // ランダムにドキュメントを取得する
        CollectionReference testNotification = db.collection("testNotification");
        Query query = testNotification
                .whereGreaterThanOrEqualTo("random", Math.random())
                .orderBy("random", Query.Direction.ASCENDING)
                .limit(1);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            random = document.getDouble("random");
            token = document.getString("token");
            uid = document.getString("uid");
        }

        System.out.println("RANDOM: " + random);
        System.out.println("TOKEN: " + token);
        System.out.println("UID: " + uid);

        resultList.set(0, token);
        resultList.set(1, uid);

        System.out.println(resultList);

        return resultList;
    }
}
