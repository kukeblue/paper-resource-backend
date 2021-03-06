package top.kukechen.paperresourcebackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kukechen.paperresourcebackend.model.Paper;
import top.kukechen.paperresourcebackend.model.Tag;
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.restservice.ResponseWrap;
import top.kukechen.paperresourcebackend.service.MongoDBUtil;
import top.kukechen.paperresourcebackend.service.PageModel;
import top.kukechen.paperresourcebackend.utils.CommonUtils;
import top.kukechen.paperresourcebackend.utils.PassToken;

import java.util.HashMap;
import java.util.List;

import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_FAILED;
import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_OK;

@PassToken
@RestController
@RequestMapping("/tag")
public class TagController {

    private static Logger logger = LoggerFactory.getLogger(TagController.class);

    @PostMapping("/add")
    @PassToken
    public Response<Paper> addTag(@RequestBody Tag tag) {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Query query = Query.query(Criteria.where("name").is(tag.getName()));
        Tag _tag = mongoTemplate.findOne(query, Tag.class);
        if (_tag != null) {
            return new Response<Paper>(STAUTS_FAILED, "标签已存在");
        }
        mongoTemplate.save(tag);
        return new Response(0, tag);
    }

    @PostMapping("/page")
    @PassToken
    public Response<Tag> getTag(@RequestBody ResponseWrap rw) {
        HashMap query = new HashMap<String, Object>();
        PageModel page = MongoDBUtil.findSortPageCondition(Tag.class, "tag", rw.getQuery(), rw.getPageNo(), rw.getPageSize(), Sort.Direction.ASC, "created");
        return new Response(STAUTS_OK, page);
    }

    @PostMapping("/list")
    @PassToken
    public Response<Tag> getTagList() {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        List<Tag> list = mongoTemplate.findAll(Tag.class, "tag");
        return new Response(STAUTS_OK, list);
    }

    @PostMapping("/delete")
    @PassToken
    public Response<Tag> deleteTag(@RequestBody ResponseWrap rw) {
        String id = rw.getId();
        logger.info("开始删除标签: " + id);
        int result = MongoDBUtil.removeById(id, "tag");
        logger.info("删除标签成功: id为" + id + " 删除结果 - result: " + result);
        if (result == 1) {
            return new Response(STAUTS_OK, "删除成功");
        } else {
            return new Response(STAUTS_FAILED, "删除失败");
        }
    }

    @PostMapping("/edit")
    @PassToken
    public Response<Paper> editTag(@RequestBody Tag tag) throws IllegalAccessException {
        MongoDBUtil.updateMulti("_id", tag.getId(), CommonUtils.getObjectToMap(tag), "tag", 1);
        return new Response(0, tag);
    }

}
