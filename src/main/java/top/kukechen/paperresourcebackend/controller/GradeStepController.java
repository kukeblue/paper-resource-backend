package top.kukechen.paperresourcebackend.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kukechen.paperresourcebackend.model.GradeStep;
import top.kukechen.paperresourcebackend.model.GradeStepQuery;
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

@RestController
@RequestMapping("/gradeStep")
public class GradeStepController {

    private static Logger logger = LoggerFactory.getLogger(GradeStepController.class);

    @PostMapping("/add")
    @PassToken
    public Response<GradeStep> addGrade(@RequestBody GradeStep gradeStep) {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        GradeStep neGradeStep = mongoTemplate.save(gradeStep);
        return new Response(0, neGradeStep);
    }
    @PostMapping("/list")
    @PassToken
    public Response<GradeStep> getGradeStepList(@RequestBody GradeStepQuery gradeStepQuery) throws IllegalAccessException {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        List<GradeStep> list = (List<GradeStep>) MongoDBUtil.findSortByParam(GradeStep.class, "gradeStep", CommonUtils.getObjectToMap(gradeStepQuery), "name", Sort.Direction.ASC);
        return new Response(STAUTS_OK, list);
    }

    @PostMapping("/delete")
    @PassToken
    public Response<GradeStep> deleteGradeStep(@RequestBody ResponseWrap rw) {
        String id = rw.getId();
        logger.info("开始删除年级: " + id);
        int result = MongoDBUtil.removeById(id, "gradeStep");
        logger.info("删除学期成功: id为" + id + " 删除结果 - result: " + result);
        if(result == 1) {
            return new Response(STAUTS_OK, "删除成功");
        }else {
            return new Response(STAUTS_FAILED, "删除失败");
        }
    }

    @PostMapping("/edit")
    @PassToken
    public Response<GradeStep> editGradeStep(@RequestBody GradeStep gradeStep) throws IllegalAccessException {
        MongoDBUtil.updateMulti("_id", gradeStep.getId(), CommonUtils.getObjectToMap(gradeStep), "gradeStep", 1);
        return new Response(0, "修改成功");
    }

    @PostMapping("/page")
    @PassToken
    public Response<GradeStep> getGradeStepPage(@RequestBody ResponseWrap rw) {
        HashMap query = new HashMap<String, Object>();
        PageModel page = MongoDBUtil.findSortPageCondition(GradeStep.class, "gradeStep", rw.getQuery(), rw.getPageNo(),  rw.getPageSize(), Sort.Direction.ASC, "created");
        return new Response(STAUTS_OK, page);
    }
}
