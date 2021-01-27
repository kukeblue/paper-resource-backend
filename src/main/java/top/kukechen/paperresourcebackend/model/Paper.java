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
    long size;
    String id;
    String name;
    String fileName;
    String fileType;
    String gradeId;
    String subjectId;
    String term;
    double price;
    String gradeStepId;
    String year;
    ArrayList tagIds = new ArrayList<String>();
    ArrayList previewLinks = new ArrayList<String>();
    ArrayList region = new ArrayList<String>();
    String file;
}
