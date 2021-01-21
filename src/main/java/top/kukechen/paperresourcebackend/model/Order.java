package top.kukechen.paperresourcebackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("order")
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseModel {
    @Id
    String id;
    String userId;
    String orderName;
    double amount;
}
