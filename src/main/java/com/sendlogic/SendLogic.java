package com.sendlogic;

import Entities.GetCheerMailRepositoryEntity;
import Infra.InitializeFirebaseSdk;
import Repositories.*;
import UseCases.Pair;
import UseCases.Validator;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.util.ArrayList;

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

        // インスタンス化
        GetCheerMailRepositoryEntity getCheerMailRepositoryEntity = new GetCheerMailRepositoryEntity();

        GetCheerMailRepository getCheerMailRepository = new GetCheerMailRepositoryImpl();
        GetTokenRepository getTokenRepository = new GetTokenRepositoryImpl();
        SaveReceiverUidRepository saveReceiverUidRepository = new SaveReceiverUidRepositoryImpl();

        Validator validator = new Validator();


        // ======= メッセージの送り先(受信者)のuidとtokenを取得 ========
        Pair tokenAndUid = getTokenRepository.getToken();
        logger.debug("after call getRegistrationTokenRepository");

        // testNotificationにドキュメントがなかった時
        // Pair(errorCode, null)
        if (tokenAndUid.right == null) {
            Object errorCode = tokenAndUid.left;
            writer.write((String) errorCode);
            return;
        }

        // uidとtokenを取得できなかった時
        String tokenAndUidExistState = validator.checkIfTokenAndUidExist(tokenAndUid);
        if (!tokenAndUidExistState.equals("E_OK")) {
            // tokenとuidが取得できなかった場合
            // エラーコードをAndroidに返す
            writer.write(tokenAndUidExistState);
            return;
        }

        // Pairからtokenとuidを取得
        Object obj = tokenAndUid.left;
        ArrayList<String> arr = (ArrayList<String>) obj;
        token = arr.get(0);
        uid = arr.get(1);
        logger.debug("token: " + token);
        logger.debug("uid: " + uid);


        // ======= Android側で送られてきたメッセージIDを取得 =======
        var params = request.getQueryParameters();
        if (params.containsKey("messageId")) {
            String messageId = params.get("messageId").get(0);

            // messageIdが取得できなかったときヴァリデーションを呼び出す
            String messageIdExistState = validator.checkIfMessageExists(messageId);
            if (!tokenAndUidExistState.equals("E_OK")) {
                // messageIdが取得できなかった場合
                // エラーコードをAndroidに返す
                writer.write(messageIdExistState);
                return;
            }
            logger.debug("messageId: " + messageId);


            // messageIdと一致するドキュメントを取得できなかった場合のヴァリデーションを呼び出す
            GetCheerMailRepositoryEntity cheerMailDocument = getCheerMailRepository.getCheerMail(messageId);
            String documentExistState = validator.checkIfDocumentExists(cheerMailDocument);
            if (!documentExistState.equals("E_OK")) {
                // messageIdと一致するドキュメントが取得できなかった場合
                // エラーコードをAndroidに返す
                writer.write(documentExistState);
                return;
            }
            // messageIdと一致するドキュメントが取得できた場合はdocumentIdとuid(受信者)をfirestoreに登録
            String documentId = cheerMailDocument.getDocumentId();
            saveReceiverUidRepository.saveUidInSendTo(documentId, uid);


            // メッセージIDを追って送信すべきメッセージと送信者の名前を取得
            messageContents = getCheerMailRepositoryEntity.getMessage();
            userName = getCheerMailRepositoryEntity.getUserName();
            logger.debug("messageContents: " + messageContents);
            logger.debug("userName: " + userName);

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
            logger.debug("Successfully sent message: " + task);
        }
    }
}
