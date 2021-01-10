package top.kukechen.paperresourcebackend.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Data
@Document("paper")
@EqualsAndHashCode(callSuper = true)
public class Paper extends BaseModel {
    String id;
    String name;
    String fileName;
    EnumFileType fileType;
    String gradeId;
    String gradeStepId;
    ArrayList previewLinks = new ArrayList<String>();
    String download;
}
