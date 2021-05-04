package com.sendlogic;

import Helper.GetCheerMailRepositoryHelper;
import Helper.GetTokenRepositoryHelper;
import Infra.InitializeFirebaseSdk;
import Repositories.*;
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
        SaveReceiverUidRepositoryImpl saveReceiverUidRepository = new SaveReceiverUidRepositoryImpl();

        writer.write("make instance\n");

        // メッセージの送り先(受信者)のuidとtokenを取得
        // 送り先(受信者)のuidをSendToに登録
        GetTokenRepositoryHelper getTokenRepositoryHelper = getTokenRepository.getToken();
        writer.write("after call getRegistrationTokenRepository\n");

        token = getTokenRepositoryHelper.getToken();
        uid = getTokenRepositoryHelper.getUid();
        writer.write("token: " + token + "\n");
        writer.write("uid: " + uid + "\n");

        // Android側で送られてきたメッセージIDを取得
        var params = request.getQueryParameters();
        if (params.containsKey("messageId")) {
            String messageId = params.get("messageId").get(0);
            writer.write("messageId: " + messageId + "\n");

            // 送信されてきたメッセージidと一致するドキュメントにSendToというサブコレクションを作成し、そこに送信先となるユーザのuidを登録する
            // TODO: デバッグ終わったらresponseを消す
            saveReceiverUidRepository.saveUidInSendTo(messageId, uid);

            // メッセージIDを追って送信すべきメッセージと送信者の名前を取得
            GetCheerMailRepositoryHelper getCheerMailRepositoryHelper = getCheerMailRepository.getMessageAndName(messageId);
            messageContents = getCheerMailRepositoryHelper.getMessage();
            userName = getCheerMailRepositoryHelper.getUserName();
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
