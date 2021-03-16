package top.kukechen.paperresourcebackend.model;
import lombok.Data;

@Data
public class ResponseUser {
    private String userName;
    private String token;
    private WxUser wxUser;

    public ResponseUser() {

    }
    public ResponseUser(String userName, String token) {
        this.userName = userName;
        this.token = token;
    }
}
