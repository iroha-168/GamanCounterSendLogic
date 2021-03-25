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
    public static List<String> getMessageInfo() throws IOException, java.lang.InterruptedException, java.util.concurrent.ExecutionException {
        String projectId = "gamancounter-8546c";
        String message = "";
        String userName = "";
        List<String> resultList = null;

        // Cloud Firestore SDK 初期化
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build();
        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();

        // TODO: messageIdを追って適切なmessageとuserNameを取得する
        String sampleMessageId = "sample_message_id";
        CollectionReference cheerMail = db.collection("cheerMail");
        Query query = cheerMail.whereEqualTo("messageId", "sampleMessageId");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            message = document.getString("message");
            userName = document.getString("userName");
        }

        resultList.add(message);
        resultList.add(userName);

        return resultList;
    }
}
