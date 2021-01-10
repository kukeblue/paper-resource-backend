package top.kukechen.paperresourcebackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Data
@Document("subject")
@EqualsAndHashCode(callSuper = true)
public class Subject extends BaseModel  {
    String id;
    String name;
}
