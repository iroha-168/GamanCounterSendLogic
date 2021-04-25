package Repositories;

import com.google.cloud.firestore.CollectionReference;

import java.util.List;

public interface GetTokenRepository {
    /**
     * getToken()は、メッセージの送信先ユーザーのtokenを取得するメソッドです
     * @return getDocumentAtRandom()の結果
     */
    public abstract List<String> getToken();

    /**
     * getMax()は、randomの最大値を取得するためのメソッドです
     * @param testNotification testNotificationコレクションのリファレンス
     * @return randomフィールドの最大値
     */
    public abstract Double getMax(CollectionReference testNotification);

    /**
     * getMax()で取得してきたmaxを利用してドキュメントをランダムで取得するメソッド
     * @param testNotification
     * @param max
     * @return ランダムに取得したuidとtokenが入ったリスト
     */
    public  abstract List<String> getDocumentAtRandom(CollectionReference testNotification, Double max);
}
