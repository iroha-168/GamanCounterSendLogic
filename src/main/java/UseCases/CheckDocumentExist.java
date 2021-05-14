package UseCases;

import Entities.GetCheerMailRepositoryEntity;
import Repositories.GetCheerMailRepository;
import Repositories.GetCheerMailRepositoryImpl;
import Repositories.SaveReceiverUidRepositoryImpl;

import java.util.concurrent.ExecutionException;

public class CheckDocumentExist {
    // 送信されてきたメッセージidと一致するドキュメントにsendToというサブコレクションを作成
    public void check(String messageId, String uid) throws InterruptedException, ExecutionException {
        SaveReceiverUidRepositoryImpl saveReceiverUidRepository = new SaveReceiverUidRepositoryImpl();
        GetCheerMailRepository getCheerMailRepository = new GetCheerMailRepositoryImpl();
        GetCheerMailRepositoryEntity result = getCheerMailRepository.getMessageAndName(messageId);

        if (result != null) {
            String documentId = result.getDocumentId();
            saveReceiverUidRepository.saveUidInSendTo(documentId, uid);
        }
    }
}
