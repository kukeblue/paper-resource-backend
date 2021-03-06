package top.kukechen.paperresourcebackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("tag")
@EqualsAndHashCode(callSuper = true)
public class Tag extends BaseModel {
    @Id
    String id;
    String name;
    String tagTypeId;
    ArrayList<String> gradeIds;
    ArrayList<String> subjectIds;
}
