package UseCases;

import Entities.GetCheerMailRepositoryEntity;
import Repositories.GetCheerMailRepository;
import Repositories.GetCheerMailRepositoryImpl;
import Repositories.SaveReceiverUidRepositoryImpl;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class CheckDocumentExist {
    // 送信されてきたメッセージidと一致するドキュメントにsendToというサブコレクションを作成
    public void check(HttpResponse response, String messageId, String uid) throws InterruptedException, ExecutionException, IOException {
        SaveReceiverUidRepositoryImpl saveReceiverUidRepository = new SaveReceiverUidRepositoryImpl();
        GetCheerMailRepository getCheerMailRepository = new GetCheerMailRepositoryImpl();
        GetCheerMailRepositoryEntity result = getCheerMailRepository.getMessageAndName(messageId);

        BufferedWriter writer = response.getWriter();

        if (result != null) {
            String documentId = result.getDocumentId();
            saveReceiverUidRepository.saveUidInSendTo(documentId, uid);
        } else {
            writer.write("E_002");
        }
    }
}
