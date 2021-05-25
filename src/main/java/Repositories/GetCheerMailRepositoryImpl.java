package Repositories;

import Entities.GetCheerMailRepositoryEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.concurrent.ExecutionException;

public class GetCheerMailRepositoryImpl implements GetCheerMailRepository {
    // 引数のmessageIdと一致するメッセージ、送信者の名前、またそれらが登録されているドキュメントのIDを取得
    public GetCheerMailRepositoryEntity getCheerMail(String messageId) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference cheerMail = db.collection("cheerMail");

        return getMessageNameDocumentId(messageId, cheerMail);
    }

    public GetCheerMailRepositoryEntity getMessageNameDocumentId(String massageId, CollectionReference cheerMail) throws InterruptedException, ExecutionException {
        String message = "";
        String userName = "";
        String documentId = "";

        // messageIdを追って適切なmessageとuserNameを取得する
        Query query = cheerMail.whereEqualTo("messageId", massageId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            message = document.getString("message");
            userName = document.getString("userName");
            documentId = document.getId();
        }

        GetCheerMailRepositoryEntity helper = new GetCheerMailRepositoryEntity();
        helper.setMessage(message);
        helper.setUserName(userName);
        helper.setDocumentId(documentId);

        return helper;

    }
}
