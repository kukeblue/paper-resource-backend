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
    String openid;
    String province;
    String unionId;
    String country;
    String city;
    String gender;
    String avatarUrl;
    String nickName;

    public WxUser(String openid) {
        this.openid = openid;
    }
}
