package Repositories;

import Helper.GetCheerMailRepositoryHelper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetCheerMailRepositoryImpl implements GetCheerMailRepository {
    // messageIdを元にcheerMailコレクションからメッセージと送信者の名前を取得する
    public GetCheerMailRepositoryHelper getMessageAndName(String messageId) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference cheerMail = db.collection("cheerMail");

        GetCheerMailRepository getCheerMailRepository = new GetCheerMailRepositoryImpl();
        return getCheerMailRepository.getCheerMail(messageId, cheerMail);
    }

    public GetCheerMailRepositoryHelper getCheerMail(String massageId, CollectionReference cheerMail) throws InterruptedException, ExecutionException {
        String message = "";
        String userName = "";
        List<String> resultList = null;

        // messageIdを追って適切なmessageとuserNameを取得する
        Query query = cheerMail.whereEqualTo("messageId", massageId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            message = document.getString("message");
            userName = document.getString("userName");
        }

        GetCheerMailRepositoryHelper helper = new GetCheerMailRepositoryHelper();
        helper.setMessage(message);
        helper.setUserName(userName);

        return helper;

    }
}
