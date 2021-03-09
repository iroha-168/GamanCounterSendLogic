package Repositories;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.util.List;

public class GetCheerMailRepository {
    public static List<String> getMessageInfo() throws IOException {
        String projectId = "gamancounter-8546c";
        List<String> resultList = null;

        // Cloud Firestore SDK 初期化
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build();
        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();

        // TODO: messageidを追って適切なmessageとuserNameを取得する
        CollectionReference cheerMail = db.collection("cheerMail");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            random = document.getDouble("random");
            token = document.getString("token");
            uid = document.getString("uid");
        }

        System.out.println("RANDOM: " + random);
        System.out.println("TOKEN: " + token);
        System.out.println("UID: " + uid);

        return token;


    }
}
