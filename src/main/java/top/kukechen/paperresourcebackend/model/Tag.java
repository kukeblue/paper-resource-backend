package top.kukechen.paperresourcebackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("tag")
@EqualsAndHashCode(callSuper = true)
public class Tag extends BaseModel {
    String name;
    String Id;
    boolean mainTag;
    List<Tag> subTags;
    String gradeId;


}
