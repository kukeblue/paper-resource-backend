package top.kukechen.paperresourcebackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("wx_user")
public class WxUser {
    @Id
    String id;
    String username;
    String phonenumber;
    String avatar;
    String wxnumber;

}
