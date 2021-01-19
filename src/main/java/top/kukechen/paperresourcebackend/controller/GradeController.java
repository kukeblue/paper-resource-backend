package top.kukechen.paperresourcebackend.controller;


import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import top.kukechen.paperresourcebackend.model.Grade;
import top.kukechen.paperresourcebackend.model.Paper;
import top.kukechen.paperresourcebackend.model.User;
import top.kukechen.paperresourcebackend.restservice.Greeting;
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.restservice.ResponseWrap;
import top.kukechen.paperresourcebackend.service.MongoDBUtil;
import top.kukechen.paperresourcebackend.service.PageModel;
import top.kukechen.paperresourcebackend.units.PassToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_FAILED;
import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_OK;

@RestController
@RequestMapping("/grade")
public class GradeController {

    private static Logger logger = LoggerFactory.getLogger(GradeController.class);

    @PostMapping("/add")
    public Response<Grade> addGrade(@RequestBody Grade grade) {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Query query = Query.query(Criteria.where("name").is(grade.getName()));
        Grade _grade = mongoTemplate.findOne(query, Grade.class);
        if(_grade != null) {
            return new Response<Grade>(STAUTS_FAILED, "年级已存在");
        }
        mongoTemplate.save(grade);
        return new Response(0, grade);
    }
    @PostMapping("/edit")
    public Response<Grade> editGrade(@RequestBody Grade grade) {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Criteria criteria = Criteria.where("_id").is(grade.getId());
        Query query = Query.query(criteria);
        Update update = new Update();
        update.set("name", grade.getName());
        UpdateResult res = mongoTemplate.updateFirst(query, update, Grade.class);
        return new Response(0, res);
    }

    @PostMapping("/list")
    public Response<Grade> getGradeList() {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        List<Grade> list = mongoTemplate.findAll(Grade.class, "grade");
        return new Response(STAUTS_OK, list);
    }

    @PostMapping("/delete")
    public Response<Grade> deleteGrade(@RequestBody ResponseWrap rw) {
        String id = rw.getId();
        logger.info("开始删除年级: " + id);
        int result = MongoDBUtil.removeById(id, "grade");
        logger.info("删除年级成功: id为" + id + " 删除结果 - result: " + result);
        if(result == 1) {
            return new Response(STAUTS_OK, "删除成功");
        }else {
            return new Response(STAUTS_FAILED, "删除失败");
        }
    }

    @PostMapping("/page")
    public Response<Grade> getGradePage(@RequestBody ResponseWrap rw) {
        HashMap query = new HashMap<String, Object>();
        PageModel page = MongoDBUtil.findSortPageCondition(Grade.class, "grade", rw.getQuery(), rw.getPageNo(),  rw.getPageSize(), Sort.Direction.ASC, "created");
        return new Response(STAUTS_OK, page);
    }
}
