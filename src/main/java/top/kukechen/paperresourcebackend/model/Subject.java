package top.kukechen.paperresourcebackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document("subject")
@EqualsAndHashCode(callSuper = true)
public class Subject extends BaseModel {
    @Id
    String id;
    String name;

}
