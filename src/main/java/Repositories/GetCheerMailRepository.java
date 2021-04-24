package Repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.List;

public class GetCheerMailRepository {
    // messageIdを元にcheerMailコレクションからメッセージと送信者の名前を取得する
    public List<String> getMessageInfo(String messageId) throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {

        String message = "";
        String userName = "";
        List<String> resultList = null;

        Firestore db = FirestoreClient.getFirestore();

        // messageIdを追って適切なmessageとuserNameを取得する
        CollectionReference cheerMail = db.collection("cheerMail");
        Query query = cheerMail.whereEqualTo("messageId", messageId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            message = document.getString("message");
            userName = document.getString("userName");
        }

        resultList.add(0, message);
        resultList.add(1, userName);

        return resultList;
    }
}
