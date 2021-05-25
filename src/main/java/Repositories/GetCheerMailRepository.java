package Repositories;

import Entities.GetCheerMailRepositoryEntity;
import com.google.cloud.firestore.CollectionReference;

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
    GetCheerMailRepositoryEntity getCheerMail(String messageId) throws InterruptedException, ExecutionException;

    /**
     * get()は、getMessageAndName()で渡されてきた引数を元に、実際にfirestoreにデータを取得しにいくメソッドです。
     *
     * @param messageId
     * @param cheerMail: cheerMailコレクションのリファレンス
     * @return 取得したmessageとuserNameとdocumentId
     * @throws InterruptedException
     * @throws ExecutionException
     */
    GetCheerMailRepositoryEntity getMessageNameDocumentId(String messageId, CollectionReference cheerMail) throws InterruptedException, ExecutionException;

}
