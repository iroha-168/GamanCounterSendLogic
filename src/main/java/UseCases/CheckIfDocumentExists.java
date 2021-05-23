package UseCases;

import Entities.GetCheerMailRepositoryEntity;
import Entities.ReturnErrorCodeEntity;

public class CheckIfDocumentExists {
    // 送信されてきたメッセージidと一致するドキュメント()が存在するかチェック
    public String check(GetCheerMailRepositoryEntity messageAndNameEqualToMassageId) {
        ReturnErrorCodeEntity returnErrorCodeEntity = new ReturnErrorCodeEntity();

        if (messageAndNameEqualToMassageId == null) {
            // messageIdと一致するドキュメントが存在しなかった場合
            String errorCode = returnErrorCodeEntity.ReturnErrorCode("cannot find document");
            return errorCode;

        } else {
            // messageIdと一致するドキュメントが存在した場合
            String successCode = returnErrorCodeEntity.ReturnErrorCode("success");
            return successCode;
        }
    }
}
