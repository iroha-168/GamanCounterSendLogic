package Repositories;

public interface SaveReceiverUidRepository {
    /**
     * saveUidInSendTo()は、送信先になるユーザのuidをcheerMailコレクションの下のSendToサブコレクションに登録するメソッドです
     *
     * @param documentId
     * @param uid
     */
     void saveUidInSendTo(String documentId, String uid);
}
