package com.sendlogic;

import Entities.GetCheerMailRepositoryEntity;
import Entities.GetTokenRepositoryEntity;
import Infra.InitializeFirebaseSdk;
import Repositories.GetCheerMailRepository;
import Repositories.GetCheerMailRepositoryImpl;
import Repositories.GetTokenRepository;
import Repositories.GetTokenRepositoryImpl;
import UseCases.CheckDocumentExist;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;

public class SendLogic implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        BufferedWriter writer = response.getWriter();
        Logger logger = LoggerFactory.getLogger(SendLogic.class);

        String token;
        String uid;
        String userName;
        String messageContents;
        String condition = "'stock-GOOG' in topics || 'industry-tech' in topics";

        InitializeFirebaseSdk.initializeSdk();

        GetCheerMailRepository getCheerMailRepository = new GetCheerMailRepositoryImpl();
        GetTokenRepository getTokenRepository = new GetTokenRepositoryImpl();
        CheckDocumentExist checkDocumentExist = new CheckDocumentExist();

        logger.debug("make instance\n");

        // メッセージの送り先(受信者)のuidとtokenを取得
        GetTokenRepositoryEntity getTokenRepositoryEntity = getTokenRepository.getToken();
        logger.debug("after call getRegistrationTokenRepository\n");

        token = getTokenRepositoryEntity.getToken();
        uid = getTokenRepositoryEntity.getUid();
        logger.debug("token: " + token + "\n");
        logger.debug("uid: " + uid + "\n");

        // uidを取得できなかったときのヴァリデーション
        if (uid == null) {
            writer.write("E_001");
        }

        // Android側で送られてきたメッセージIDを取得
        var params = request.getQueryParameters();
        if (params.containsKey("messageId")) {
            String messageId = params.get("messageId").get(0);

            // messageIdが取得できなかったときのためのヴァリデーション
            checkDocumentExist.check(response, messageId, uid);

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
