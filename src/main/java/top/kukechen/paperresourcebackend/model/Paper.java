package top.kukechen.paperresourcebackend.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("paper")
@EqualsAndHashCode(callSuper=true)
public class Paper extends BaseModel {
    String fileName;
    EnumFileType fileType;
    String gradeId;
    String gradeStepId;
}
