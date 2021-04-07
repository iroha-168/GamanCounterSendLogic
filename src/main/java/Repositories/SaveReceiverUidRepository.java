package Repositories;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;

public class SaveReceiverUidRepository {
    // 送り先のuidをsendTosサブコレクションに保存
    public static void saveUidInSendTo(String uid) throws IOException {
//        String projectId = "gamancounter-8546c";

//        // Cloud Firestore SDK 初期化
//        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(credentials)
//                .setProjectId(projectId)
//                .build();
//        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();

        // cheerMailコレクション配下のsendToコレクションにuidを送信先ユーザーのuidを保存
        db.collection("cheerMail")
                .document()
                .collection("sendTo")
                .add(uid);
    }
}
