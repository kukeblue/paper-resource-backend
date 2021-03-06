package top.kukechen.paperresourcebackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@EqualsAndHashCode(callSuper=true)
@Document("gradeStep")
public class GradeStep extends BaseModel {
    @Id
    String id;
    String gradeId;
    String name;
    String alias;
}


