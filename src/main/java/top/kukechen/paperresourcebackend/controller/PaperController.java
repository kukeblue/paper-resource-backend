package top.kukechen.paperresourcebackend.controller;

import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.kukechen.paperresourcebackend.model.Grade;
import top.kukechen.paperresourcebackend.model.Paper;
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.restservice.ResponseWrap;
import top.kukechen.paperresourcebackend.service.MongoDBUtil;
import top.kukechen.paperresourcebackend.service.PageModel;
import top.kukechen.paperresourcebackend.units.FileTypeUtils;
import top.kukechen.paperresourcebackend.units.FileUpload;
import top.kukechen.paperresourcebackend.units.PassToken;
import top.kukechen.paperresourcebackend.units.PdfUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_FAILED;
import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_OK;

@PassToken
@RestController
@RequestMapping("/paper")
public class PaperController {

    private static Logger logger = LoggerFactory.getLogger(PaperController.class);

    @Value("${upload.path}")
    private String uploadPath;


    @PostMapping("/upload")
    public Response standardServletMultipartResolver(@RequestParam(name = "files") MultipartFile[] files ) {

        logger.info("检测到文件上传，上传启动...");
        String fileType = "";
        String result = "未检测到内容";
        for (MultipartFile file:files) {
            File directory = FileUpload.buildDirectory();
            File paper = FileUpload.upload(file, directory);
            if(paper != null) {
                fileType = FileTypeUtils.getFileTypeByFile(paper);
            }
        }
//        if(fileType == "pdf") {
//            String text = PdfUtils.getPdfFirstPage(paper);
//            if(!text.isEmpty()) {
//                result = text.split("\n")[0];
//            }
//        }
        return new Response(result, "上传成功");
    }


    @PostMapping("/add")
    @PassToken
    public Response<Paper> addPaper(@RequestBody Paper paper) {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Query query = Query.query(Criteria.where("name").is(paper.getName()));
        Paper _paper = mongoTemplate.findOne(query, Paper.class);
        if (_paper != null) {
            return new Response<Paper>(STAUTS_FAILED, "文件已存在");
        }
        mongoTemplate.save(paper);
        return new Response(0, paper);
    }

    @PostMapping("/list")
    @PassToken
    public Response<Grade> getPaperList() {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        List<Paper> list = mongoTemplate.findAll(Paper.class, "paper");
        return new Response(STAUTS_OK, list);
    }

    @PostMapping("/delete")
    @PassToken
    public Response<Grade> deletePaper(@RequestBody ResponseWrap rw) {
        String id = rw.getId();
        logger.info("开始删除试卷: " + id);
        int result = MongoDBUtil.removeById(id, "paper");
        logger.info("删除试卷成功: id为" + id + " 删除结果 - result: " + result);
        if (result == 1) {
            return new Response(STAUTS_OK, "删除成功");
        } else {
            return new Response(STAUTS_FAILED, "删除失败");
        }
    }


    @PostMapping("/page")
    @PassToken
    public Response<Grade> getGradePage(@RequestBody ResponseWrap rw) {
        HashMap query = new HashMap<String, Object>();
        PageModel page = MongoDBUtil.findSortPageCondition(Paper.class, "paper", rw.getQuery(), rw.getPageNo(), rw.getPageSize(), Sort.Direction.ASC, "created");
        return new Response(STAUTS_OK, page);
    }

    @PostMapping("/edit")
    @PassToken
    public Response<Paper> editGrade(@RequestBody Paper paper) {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Criteria criteria = Criteria.where("_id").is(paper.getId());
        Query query = Query.query(criteria);
        Update update = new Update();
        update.set("name", paper.getName());
        UpdateResult res = mongoTemplate.updateFirst(query, update, Paper.class);
        return new Response(0, res);
    }


//    @PostMapping("/edit")
//    @PassToken
//    public Response<Grade> editGrade(@RequestBody Grade grade) {
//        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
//        Criteria criteria = Criteria.where("_id").is(grade.getId());
//        Query query = Query.query(criteria);
//        Update update = new Update();
//        update.set("name", grade.getName());
//        UpdateResult res = mongoTemplate.updateFirst(query, update, Grade.class);
//        return new Response(0, res);
//    }
//
//    @PostMapping("/list")
//    @PassToken
//    public Response<Grade> getGradeList() {
//        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
//        List<Grade> list = mongoTemplate.findAll(Grade.class, "grade");
//        return new Response(STAUTS_OK, list);
//    }
//
//    @PostMapping("/delete")
//    @PassToken
//    public Response<Grade> deleteGrade(@RequestBody ResponseWrap rw) {
//        String id = rw.getId();
//        logger.info("开始删除年级: " + id);
//        int result = MongoDBUtil.removeById(id, "grade");
//        logger.info("删除年级成功: id为" + id + " 删除结果 - result: " + result);
//        if(result == 1) {
//            return new Response(STAUTS_OK, "删除成功");
//        }else {
//            return new Response(STAUTS_FAILED, "删除失败");
//        }
//    }
}
