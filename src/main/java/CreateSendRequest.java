import Repositories.GetRegistrationTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.util.List;

public class CreateSendRequest {

    public static void main(String[] args) throws FirebaseMessagingException {

        String token;
        String condition = "'stock-GOOG' in topics || 'industry-tech' in topics";
        // TODO: getCheerMailRepositoryをインスタンス化
        GetRegistrationTokenRepository getRegistrationTokenRepository = new GetRegistrationTokenRepository();

        List<TestNotificationDataClass> resultList = getRegistrationTokenRepository.getToken();
        System.out.println(resultList);

        // TODO: チアメールとユーザー名を受け取る
        token = resultList.get(0).toString();
        System.out.println(token);

        // 受け取ったトークンを使い、特定のデバイスにメッセージを送信する処理かく
        String registrationToken = token;

        Message message = Message.builder()
                .setNotification(Notification.builder()
                .setTitle("さんからチアメールが届きました") // TODO: ユーザー名を入れる
                .setBody("message contents") //TODO: メッセージ入れる
                .build())
                .setCondition(condition)
                .setToken(registrationToken)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Successfully sent message:" + response);
    }
}
