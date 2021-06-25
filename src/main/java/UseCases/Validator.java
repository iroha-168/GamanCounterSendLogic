package UseCases;

import Entities.GetCheerMailRepositoryEntity;
import Entities.GetTokenRepositoryEntity;
import Entities.ReturnErrorCodeEntity;

public class Validator {
    public String checkIfTokenAndUidExist(GetTokenRepositoryEntity tokenAndUid) {
        ReturnErrorCodeEntity returnErrorCodeEntity = new ReturnErrorCodeEntity();

        if (tokenAndUid == null) {
            // tokenとuidが取得できなかった場合
            String errorCode = returnErrorCodeEntity.ReturnErrorCode("cannot get token and uid");
            return errorCode;

        } else {
            // tokenとuidが取得できた場合
            String successCode = returnErrorCodeEntity.ReturnErrorCode("success");
            return successCode;
        }
    }

    public String checkIfMessageExists(String messageId) {
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

    public String checkIfDocumentExists(GetCheerMailRepositoryEntity messageAndNameEqualToMassageId) {
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
