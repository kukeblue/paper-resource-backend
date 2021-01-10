package top.kukechen.paperresourcebackend.controller;

import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.restservice.ResponseWrap;
import top.kukechen.paperresourcebackend.service.MongoDBUtil;
import top.kukechen.paperresourcebackend.units.PassToken;

import javax.security.auth.Subject;

import java.util.List;

import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_FAILED;
import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_OK;

@PassToken
@RestController
@RequestMapping("/subject")
public class SubjectController {
    private static Logger logger = LoggerFactory.getLogger(SubjectController.class);


    @PostMapping("/add")
    @PassToken
    public Response<Paper> addSubject(@RequestBody Subject subject) {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Query query = Query.query(Criteria.where("name").is(subject.getName()));
        Subject _subject = mongoTemplate.findOne(query, Subject.class);
        if (_subject != null) {
            return new Response<Paper>(STAUTS_FAILED, "学科已存在");
        }
        mongoTemplate.save(subject);
        return new Response(0, subject);
    }

    @PostMapping("/subject")
    @PassToken
    public Response<Grade> getSubjectList() {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        List<Subject> list = mongoTemplate.findAll(Subject.class, "subject");
        return new Response(STAUTS_OK, list);
    }

    @PostMapping("/delete")
    @PassToken
    public Response<Grade> deleteSubject(@RequestBody ResponseWrap rw) {
        String id = rw.getId();
        logger.info("开始删除学科: " + id);
        int result = MongoDBUtil.removeById(id, "subject");
        logger.info("删除学科成功: id为" + id + " 删除结果 - result: " + result);
        if (result == 1) {
            return new Response(STAUTS_OK, "删除成功");
        } else {
            return new Response(STAUTS_FAILED, "删除失败");
        }
    }

    @PostMapping("/edit")
    @PassToken
    public Response<Paper> editGrade(@RequestBody Subject subject) {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Criteria criteria = Criteria.where("_id").is(subject.getId());
        Query query = Query.query(criteria);
        Update update = new Update();
        update.set("name", subject.getName());
        UpdateResult res = mongoTemplate.updateFirst(query, update, Subject.class);
        return new Response(0, res);
    }

}
