package top.kukechen.paperresourcebackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WxResponse {
    @JsonProperty("session_key")
    private String sessionKey;
    private String openid;
}
