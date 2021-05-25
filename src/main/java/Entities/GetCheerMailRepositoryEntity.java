package Entities;

public class GetCheerMailRepositoryEntity {
    private String message;
    private String userName;
    private String documentId;

    // ------ getter ------
    public String getMessage() {
        return message;
    }
    public String getUserName() {
        return userName;
    }
    public String getDocumentId() { return documentId; }

    // ------ setter ------
    public void setMessage(String message) {
        this.message = message;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
