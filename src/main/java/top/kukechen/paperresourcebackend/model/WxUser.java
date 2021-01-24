package top.kukechen.paperresourcebackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper=false)
@Document("wx_user")
public class WxUser extends BaseModel {
    @Id
    String id;
    String openid;
    String province;
    String country;
    String city;
    int gender;
    String language;
    String avatarUrl;
    String nickName;

    public WxUser() {
    }

    public WxUser(String openid) {
        this.openid = openid;
    }
}
