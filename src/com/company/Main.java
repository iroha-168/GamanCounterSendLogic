package com.company;

public class Main {

    public static void main(String[] args) {
        // 送信リクエストを認証する
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);
    }
}
