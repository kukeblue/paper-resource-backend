package top.kukechen.paperresourcebackend.model;
import lombok.Data;

import java.util.Date;


@Data
public class BaseModel {
    Date created = new Date();
    Date updateTime = new Date();
}
