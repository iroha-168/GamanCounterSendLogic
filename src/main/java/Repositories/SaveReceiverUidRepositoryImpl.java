package Repositories;

import Helper.GetCheerMailRepositoryHelper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class SaveReceiverUidRepositoryImpl {
    // 送信されてきたメッセージidと一致するドキュメントにsendToというサブコレクションを作成
    // sendToサブコレクションに送信先となるユーザのuidを登録
    public void saveUidInSendTo(String messageId, String uid) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();

        // getCheerMailRepositoryを利用して引数のmessageIdと一致するドキュメントを取得
        GetCheerMailRepository getCheerMailRepository = new GetCheerMailRepositoryImpl();
        GetCheerMailRepositoryHelper result = getCheerMailRepository.getMessageAndName(messageId);

        if (result != null) {
            // 引数のmessageIdと一致するドキュメントが取得できた場合
            String documentId = result.getDocumentId();
            Logger logger = LoggerFactory.getLogger(SaveReceiverUidRepositoryImpl.class);
            logger.info("documentId: " + documentId);

            db.collection("cheerMail").document(documentId).collection("sendTo").add(uid);
        }
    }
}
