package Repositories;

import Helper.GetCheerMailRepositoryHelper;
import com.google.cloud.firestore.CollectionReference;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface GetCheerMailRepository {
    /**
     * getMessageAndName()は、get()で取得した名前とメッセージ内容を呼び出し元に返すメソッドです
     *
     * @param messageId
     * @return get()の結果であるmessageとuserNameとdocumentId
     * @throws InterruptedException
     * @throws ExecutionException
     */
    GetCheerMailRepositoryHelper getMessageAndName(String messageId) throws InterruptedException, ExecutionException;

    /**
     * get()は、getMessageAndName()で渡されてきた引数を元に、実際にfirestoreにデータを取得しにいくメソッドです。
     *
     * @param messageId
     * @param cheerMail: cheerMailコレクションのリファレンス
     * @return 取得したmessageとuserNameとdocumentId
     * @throws InterruptedException
     * @throws ExecutionException
     */
    GetCheerMailRepositoryHelper getCheerMail(String messageId, CollectionReference cheerMail) throws InterruptedException, ExecutionException;

}
