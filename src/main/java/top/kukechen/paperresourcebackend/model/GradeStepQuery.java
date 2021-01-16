package top.kukechen.paperresourcebackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class GradeStepQuery {
    String id;
    String gradeId;
    String name;
    EnumTerm term;  // 学年up为上学年， down为下学年
}
