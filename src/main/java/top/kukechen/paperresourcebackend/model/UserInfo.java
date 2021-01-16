package top.kukechen.paperresourcebackend.model;


import lombok.Data;

@Data
public class UserInfo {
    private String id;
    private String username;

    public void asyncUser(User user) {
    }
}
