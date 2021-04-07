package Infra;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.IOException;

// Cloud Firestore SDK 初期化
public class InitializeFirebaseSdk {
    public static void initializeSdk() throws IOException {
        String projectId = "gamancounter-8546c";

        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        System.out.println("credentials: " + credentials);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build();
        FirebaseApp.initializeApp(options);
    }
}
