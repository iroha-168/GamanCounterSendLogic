package UseCases;

import Entities.GetTokenRepositoryEntity;
import Entities.ReturnErrorCodeEntity;

public class CheckIfTokenAndUidExist {
    public String check(GetTokenRepositoryEntity tokenAndUid) {
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
}
