package top.kukechen.paperresourcebackend.restservice;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class SortWarp {
    private String value = "created";
    private Sort.Direction direction = Sort.Direction.DESC;
}
