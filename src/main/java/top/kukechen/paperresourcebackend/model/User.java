package top.kukechen.paperresourcebackend.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


@Data
@Document("user")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private UserInfo  userInfo = new UserInfo();

    public User() {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        userInfo.setId(this.id);
    }

    public void setUsername(String username) {
        userInfo.setUsername(username);
        this.username = username;
    }
}


