package UseCases;

import Entities.GetCheerMailRepositoryEntity;
import Entities.ReturnErrorCodeEntity;
import Repositories.GetCheerMailRepository;
import Repositories.GetCheerMailRepositoryImpl;
import Repositories.SaveReceiverUidRepositoryImpl;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class CheckDocumentExist {
    // 送信されてきたメッセージidと一致するドキュメントにsendToというサブコレクションを作成
    public String check(String messageId, String uid) throws InterruptedException, ExecutionException, IOException {
        SaveReceiverUidRepositoryImpl saveReceiverUidRepository = new SaveReceiverUidRepositoryImpl();
        GetCheerMailRepository getCheerMailRepository = new GetCheerMailRepositoryImpl();

        GetCheerMailRepositoryEntity result = getCheerMailRepository.getMessageAndName(messageId);

        if (result != null) {
            // messageIdと一致するドキュメントが存在した場合
            // そのドキュメントにuidを登録
            String documentId = result.getDocumentId();
            saveReceiverUidRepository.saveUidInSendTo(documentId, uid);
        } else {
            // messageIdと一致するドキュメントが存在しなかった場合
            ReturnErrorCodeEntity returnErrorCodeEntity = new ReturnErrorCodeEntity();
            String errorCode = returnErrorCodeEntity.ReturnErrorCode("cannot find document");
            return errorCode;
        }
        return null;
    }
}
