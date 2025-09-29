package Entities;

public class ReturnErrorCodeEntity {
    public String ReturnErrorCode(String errorMsg) {
        switch (errorMsg) {
            case "cannot get token and uid":
                return "E_001";

            case "cannot get messageId":
                return "E_002";

            case "cannot find document":
                return "E_003";

            case "success":
                return "E_OK";
        }
        return "E_004";
    }
}
