package Repositories;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

public class SaveReceiverUidRepositoryImpl {
    // sendToサブコレクションに送信先となるユーザのuidを登録

    public void saveUidInSendTo(String documentId, String uid) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("cheerMail").document(documentId).collection("sendTo").add(uid);
    }
}
