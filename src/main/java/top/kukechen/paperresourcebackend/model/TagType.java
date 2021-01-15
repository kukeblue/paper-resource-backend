package top.kukechen.paperresourcebackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("tagType")
@EqualsAndHashCode(callSuper = true)
public class TagType extends BaseModel{
    private String id;
    private String name;
}
