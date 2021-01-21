package top.kukechen.paperresourcebackend.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Older extends BaseModel {
    @Id
    String userId;
    String olderId;
    double amount;
}
