package com.sendlogic;

import Repositories.GetCheerMailRepository;
import Repositories.GetRegistrationTokenRepository;
import Repositories.SaveReceiverUidRepository;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.util.List;

public class sendLogic implements HttpFunction {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {

        String token;
        String uid;
        String userName;
        String messageContents;
        String condition = "'stock-GOOG' in topics || 'industry-tech' in topics";

        GetCheerMailRepository getCheerMailRepository = new GetCheerMailRepository();
        GetRegistrationTokenRepository getRegistrationTokenRepository = new GetRegistrationTokenRepository();
        SaveReceiverUidRepository saveReceiverUidRepository = new SaveReceiverUidRepository();

        // メッセージの送り先(受信者)のuidとtokenを取得
        // 送り先(受信者)のuidをSendToに登録
        List<String> getTokenResults = getRegistrationTokenRepository.getToken();
        token = getTokenResults.get(0);
        uid = getTokenResults.get(1);
        System.out.println("token: " + token);
        System.out.println("uid: " + uid);
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
