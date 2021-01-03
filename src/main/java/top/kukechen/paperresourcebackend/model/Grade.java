package top.kukechen.paperresourcebackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper=true)
@Document("grade")
public class Grade extends BaseModel {
    @Id
    String id;
    String name;
    @Transient
    ArrayList gradeStepList = new ArrayList<GradeStep>();
}
