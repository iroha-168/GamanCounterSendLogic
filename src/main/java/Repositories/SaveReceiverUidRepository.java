package Repositories;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.functions.HttpResponse;
import com.google.firebase.cloud.FirestoreClient;
import java.io.BufferedWriter;
import java.io.IOException;

public class SaveReceiverUidRepository {
    // TODO: 送信されてきたメッセージidと一致するドキュメントにSendToというサブコレクションを作成
    // TODO: そこに送信先となるユーザのuidを登録
    public static void saveUidInSendTo(HttpResponse response , String messageId, String uid) throws IOException {
        BufferedWriter writer = response.getWriter();
        Firestore db = FirestoreClient.getFirestore();

        // if--GETパラメータで渡されて来たmessageIdと等しいmessageIdをもつドキュメントを取得できたら(GetCheerMailRepositoryを利用する)
            // then--SendToサブコレクションを作成し、そこに仮引数のuidを登録
        // else--何かしらのエラーメッセージを返す？？


//        db.collection("cheerMail")
//                .document()
//                .collection("sendTo")
//                .add(uid);
    }
}
