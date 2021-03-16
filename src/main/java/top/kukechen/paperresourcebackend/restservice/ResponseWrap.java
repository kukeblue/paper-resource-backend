package top.kukechen.paperresourcebackend.restservice;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.Map;

@Data
public class ResponseWrap {
    private String id;
    private Map<String, Object>  query;
    private SortWarp sort = new SortWarp();
    private String keyword = "";
    int pageNo = 1;
    int pageSize = 20;
}
