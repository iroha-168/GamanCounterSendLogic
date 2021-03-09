import Repositories.GetCheerMailRepository;
import Repositories.GetRegistrationTokenRepository;
import Repositories.SaveReceiverUidRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CreateSendRequest {

    public static void main(String[] args) throws FirebaseMessagingException, IOException, ExecutionException, InterruptedException {

        List<String> result;
        String token;
        String uid;
        String userName = null;
        String messageContents = null;
        String condition = "'stock-GOOG' in topics || 'industry-tech' in topics";

        GetCheerMailRepository getCheerMailRepository = new GetCheerMailRepository();
        GetRegistrationTokenRepository getRegistrationTokenRepository = new GetRegistrationTokenRepository();
        SaveReceiverUidRepository saveReceiverUidRepository = new SaveReceiverUidRepository();

        result = getRegistrationTokenRepository.getToken();
        token = result.get(0);
        uid = result.get(1);
        System.out.println(token);
        System.out.println(uid);

        saveReceiverUidRepository.saveUidInSendTo(uid);
        List<String> resultList = getCheerMailRepository.getMessageInfo();
        // TODO: チアメールとユーザー名を受け取る

        // 特定のデバイスにメッセージを送信
        String registrationToken = token;

        Message message = Message.builder()
                .setNotification(Notification.builder()
                .setTitle(userName + "さんからチアメールが届きました") // TODO: ユーザー名を入れる
                .setBody(messageContents) //TODO: メッセージ入れる
                .build())
                .setCondition(condition)
                .setToken(registrationToken)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Successfully sent message:" + response);
    }
}
