package UseCases;

import Entities.ReturnErrorCodeEntity;

public class CheckIfMessageIdExists {
    public String check(String messageId) {
        ReturnErrorCodeEntity returnErrorCodeEntity = new ReturnErrorCodeEntity();

        if (messageId == null) {
            // messageIdと一致するドキュメントが存在しなかった場合
            String errorCode = returnErrorCodeEntity.ReturnErrorCode("cannot get messageId");
            return errorCode;

        } else {
            // messageIdと一致するドキュメントが存在した場合
            String successCode = returnErrorCodeEntity.ReturnErrorCode("success");
            return successCode;
        }
    }
}
