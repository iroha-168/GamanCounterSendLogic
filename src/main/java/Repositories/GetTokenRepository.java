package Repositories;

import com.google.cloud.firestore.CollectionReference;

import java.util.List;

public interface GetTokenRepository {
    // tokenを取得する
    public abstract List<String> getToken();

    // randomの最大値を取得する
    public abstract Double getMax(CollectionReference testNotification);

    // 取得した最大値を使ってドキュメントをランダムに取得する
    public  abstract List<String> getDocumentAtRandom(CollectionReference testNotification, Double max);
}
