package com.sendlogic;

import Entities.GetCheerMailRepositoryEntity;
import Entities.GetTokenRepositoryEntity;
import Infra.InitializeFirebaseSdk;
import Repositories.*;
import UseCases.CheckDocumentExist;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.io.BufferedWriter;

public class SendLogic implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        BufferedWriter writer = response.getWriter();
        writer.write("start sendLogic\n");
        writer.write(response.toString() + "\n");

        String token;
        String uid;
        String userName;
        String messageContents;
        String condition = "'stock-GOOG' in topics || 'industry-tech' in topics";

        InitializeFirebaseSdk.initializeSdk();

        GetCheerMailRepository getCheerMailRepository = new GetCheerMailRepositoryImpl();
        GetTokenRepository getTokenRepository = new GetTokenRepositoryImpl();
//        SaveReceiverUidRepositoryImpl saveReceiverUidRepository = new SaveReceiverUidRepositoryImpl();
        CheckDocumentExist checkDocumentExist = new CheckDocumentExist();

        writer.write("make instance\n");

        // メッセージの送り先(受信者)のuidとtokenを取得
        // TODO: uidを取得できなかったときのヴァリデーション
        GetTokenRepositoryEntity getTokenRepositoryEntity = getTokenRepository.getToken();
        writer.write("after call getRegistrationTokenRepository\n");

        token = getTokenRepositoryEntity.getToken();
        uid = getTokenRepositoryEntity.getUid();
        writer.write("token: " + token + "\n");
        writer.write("uid: " + uid + "\n");

        // Android側で送られてきたメッセージIDを取得
        // TODO: messageIdが取得できなかったときのためのヴァリデーション
        var params = request.getQueryParameters();
        if (params.containsKey("messageId")) {
            String messageId = params.get("messageId").get(0);
            writer.write("messageId: " + messageId + "\n");

            // messageIdが取得できたこのタイミングでUseCaseのcheck()を呼び出す
            checkDocumentExist.check(messageId, uid);

            // メッセージIDを追って送信すべきメッセージと送信者の名前を取得
            GetCheerMailRepositoryEntity getCheerMailRepositoryEntity = getCheerMailRepository.getMessageAndName(messageId);
            messageContents = getCheerMailRepositoryEntity.getMessage();
            userName = getCheerMailRepositoryEntity.getUserName();
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
