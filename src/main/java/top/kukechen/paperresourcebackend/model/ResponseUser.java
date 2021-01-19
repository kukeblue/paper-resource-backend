package top.kukechen.paperresourcebackend.model;

public class ResponseUser {
    private String userName;
    private String token;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ResponseUser(String userName, String token) {
        this.userName = userName;
        this.token = token;
    }
}
