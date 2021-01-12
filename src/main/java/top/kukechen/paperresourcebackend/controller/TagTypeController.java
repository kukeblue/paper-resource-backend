package top.kukechen.paperresourcebackend.controller;

import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kukechen.paperresourcebackend.model.Grade;
import top.kukechen.paperresourcebackend.model.Paper;
import top.kukechen.paperresourcebackend.model.TagType;
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.restservice.ResponseWrap;
import top.kukechen.paperresourcebackend.service.MongoDBUtil;
import top.kukechen.paperresourcebackend.service.PageModel;
import top.kukechen.paperresourcebackend.units.PassToken;

import java.util.HashMap;
import java.util.List;

import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_FAILED;
import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_OK;

@PassToken
@RestController
@RequestMapping("/tagType")
public class TagTypeController {

    private static Logger logger = LoggerFactory.getLogger(TagTypeController.class);

    @PostMapping("/add")
    @PassToken
    public Response<Paper> addSubject(@RequestBody TagType tagType) {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Query query = Query.query(Criteria.where("name").is(tagType.getName()));
        TagType _tag = mongoTemplate.findOne(query, TagType.class);
        if (_tag != null) {
            return new Response<Paper>(STAUTS_FAILED, "标签已存在");
        }
        mongoTemplate.save(tagType);
        return new Response(0, tagType);
    }

    @PostMapping("/page")
    @PassToken
    public Response<Grade> getGradePage(@RequestBody ResponseWrap rw) {
        HashMap query = new HashMap<String, Object>();
        PageModel page = MongoDBUtil.findSortPageCondition(TagType.class, "tagType", rw.getQuery(), rw.getPageNo(), rw.getPageSize(), Sort.Direction.ASC, "created");
        return new Response(STAUTS_OK, page);
    }

    @PostMapping("/list")
    @PassToken
    public Response<Grade> getTagList() {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        List<TagType> list = mongoTemplate.findAll(TagType.class, "tagType");
        return new Response(STAUTS_OK, list);
    }

    @PostMapping("/delete")
    @PassToken
    public Response<TagType> deleteTag(@RequestBody ResponseWrap rw) {
        String id = rw.getId();
        logger.info("开始删除标签: " + id);
        int result = MongoDBUtil.removeById(id, "tagType");
        logger.info("删除标签成功: id为" + id + " 删除结果 - result: " + result);
        if (result == 1) {
            return new Response(STAUTS_OK, "删除成功");
        } else {
            return new Response(STAUTS_FAILED, "删除失败");
        }
    }

    @PostMapping("/edit")
    @PassToken
    public Response<Paper> editGrade(@RequestBody TagType tagType) {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Criteria criteria = Criteria.where("_id").is(tagType.getId());
        Query query = Query.query(criteria);
        Update update = new Update();
        update.set("name", tagType.getName());
        UpdateResult res = mongoTemplate.updateFirst(query, update, TagType.class);
        return new Response(0, res);
    }

}
