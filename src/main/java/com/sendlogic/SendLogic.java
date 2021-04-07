package com.sendlogic;

import Infra.InitializeFirebaseSdk;
import Repositories.GetCheerMailRepository;
import Repositories.GetRegistrationTokenRepository;
import Repositories.SaveReceiverUidRepository;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.io.BufferedWriter;
import java.util.List;
import java.util.logging.Logger;

public class SendLogic implements HttpFunction {
    private static final Logger logger = Logger.getLogger(SendLogic.class.getName());

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        BufferedWriter writer = response.getWriter();
        writer.write("start sendLogic\n");

        String token;
        String uid;
        String userName;
        String messageContents;
        String condition = "'stock-GOOG' in topics || 'industry-tech' in topics";

        InitializeFirebaseSdk.initializeSdk();
        GetCheerMailRepository getCheerMailRepository = new GetCheerMailRepository();
        GetRegistrationTokenRepository getRegistrationTokenRepository = new GetRegistrationTokenRepository();
        SaveReceiverUidRepository saveReceiverUidRepository = new SaveReceiverUidRepository();

        writer.write("make instance\n");
//        System.out.println("make instance\n");
//        logger.info("make instance\n");

        // メッセージの送り先(受信者)のuidとtokenを取得
        // 送り先(受信者)のuidをSendToに登録
        List<String> getTokenResults = getRegistrationTokenRepository.getToken(response);
        writer.write("after call getRegistrationTokenRepository\n");

        token = getTokenResults.get(0);
        uid = getTokenResults.get(1);
        writer.write(token);
        writer.write(uid);
        saveReceiverUidRepository.saveUidInSendTo(uid);

        // Android側で送られてきたメッセージIDを取得
        var params = request.getQueryParameters();
        if (params.containsKey("messageId")) {
            String messageId = params.get("messageId").get(0);
            System.out.println("messageId: " + messageId);

            // メッセージIDを追って送信すべきメッセージと送信者の名前を取得
            List<String> getMessageResults = getCheerMailRepository.getMessageInfo(messageId);
            messageContents = getMessageResults.get(0);
            userName = getMessageResults.get(1);
            System.out.println("messageContents: " + messageContents);
            System.out.println("userName: " + userName);

            // 特定のデバイスにメッセージを送信する(プッシュ通知)
            String registrationToken = token;

            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(userName + "さんからチアメールが届きました")
                            .setBody(messageContents)
                            .build())
                    .setCondition(condition)
                    .setToken(registrationToken)
                    .build();

            String task = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + task);
        }
    }
}
