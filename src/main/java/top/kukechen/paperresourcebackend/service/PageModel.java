package top.kukechen.paperresourcebackend.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class PageModel<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 当前页
     */
    private Integer pageNo = 1;
    /**
     * 当前页条数
     */
    private Integer pageSize = 10;
    /**
     * 总共的条数
     */
    private Long total;
    /**
     * 总共的页数
     */
    private Integer pages;
    /**
     * 实体类集合
     */
    private List<T> list;

}