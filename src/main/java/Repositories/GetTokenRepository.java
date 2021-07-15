package Repositories;

import UseCases.Pair;
import com.google.cloud.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public interface GetTokenRepository {
    /**
     * getToken()は、メッセージの送信先ユーザーのtokenを取得するメソッドです
     *
     * @return getRandomUserToken()の結果である、ランダムに取得したuidとtoken
     * @throws InterruptedException
     * @throws ExecutionException
     */
    Pair<ArrayList<String>, Boolean> getToken() throws InterruptedException, ExecutionException;

    /**
     * getMax()は、randomの最大値を取得するためのメソッドです
     *
     * @param testNotification: testNotificationコレクションのリファレンス
     * @return 成功したかどうかの判定基準
     * @throws InterruptedException
     * @throws ExecutionException
     */
    Double getMax(CollectionReference testNotification) throws InterruptedException, ExecutionException;

    /**
     * getMax()で取得してきたmaxを利用してドキュメントをランダムで取得するメソッド
     *
     * @param testNotification
     * @param max
     * @return ランダムに取得してきたドキュメントの中のuidとtoken
     * @throws InterruptedException
     * @throws ExecutionException
     */
    Pair<ArrayList<String>, Boolean> getRandomUserToken(CollectionReference testNotification, Double max) throws InterruptedException, ExecutionException;
}
