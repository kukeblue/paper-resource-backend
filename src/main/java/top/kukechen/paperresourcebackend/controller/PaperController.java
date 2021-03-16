package top.kukechen.paperresourcebackend.controller;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.kukechen.paperresourcebackend.model.*;
import top.kukechen.paperresourcebackend.qiniu.UploadFile;
import top.kukechen.paperresourcebackend.qiniu.UploadFileQiniu;
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.restservice.ResponseWrap;
import top.kukechen.paperresourcebackend.service.FileAnalysis;
import top.kukechen.paperresourcebackend.service.MongoDBUtil;
import top.kukechen.paperresourcebackend.service.PageModel;
import top.kukechen.paperresourcebackend.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import top.kukechen.paperresourcebackend.qiniu.UploadFile;
import top.kukechen.paperresourcebackend.qiniu.UploadFileQiniu;
import top.kukechen.paperresourcebackend.qiniu.UploadProperties;
import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_FAILED;
import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_OK;

@RestController
@RequestMapping("/paper")
public class PaperController {

    private static Logger logger = LoggerFactory.getLogger(PaperController.class);

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    UploadProperties uploadProperties;

    @Autowired
    FileAnalysis fileAnalysis;

    @PostMapping("/upload")
    public Response upload(@RequestParam(name = "files") MultipartFile[] files ) {
        logger.info("检测到文件上传，上传启动...");
        ArrayList uploadFiles = new ArrayList();
        for (MultipartFile file:files) {
            UploadFile uploadFile = new UploadFileQiniu(uploadProperties.getQiniu());
            String fileUrl = uploadFile.uploadFile(file);
            uploadFiles.add(fileUrl);
        }
        return new Response(uploadFiles, "上传成功");
    }

    @PostMapping("/smart_upload")
    public Response smartUpload (@RequestParam(name = "files") MultipartFile[] files ) throws IOException {
        logger.info("启动文件智能上传......");
        Paper paper = new Paper();
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String[] splitName = fileName.split("\\.");
            String name = splitName[0];
            String fileType = splitName[1];
            Query query = Query.query(Criteria.where("name").is(name));
            Paper _paper = mongoTemplate.findOne(query, Paper.class);
            if (_paper != null) {
                break;
            }
            UploadFile uploadFile = new UploadFileQiniu(uploadProperties.getQiniu());
            String fileUrl = uploadFile.uploadFile(file);
            paper.setFile(fileUrl);
            String rgex = "http://kuke-static\\.kukechen\\.top/(.*?)\\.pdf";
            Pattern pattern = Pattern.compile(rgex);// 匹配的模式
            Matcher m = pattern.matcher(fileUrl);
            if(m.find()) {
                paper.setFileName(m.group(1));
            }
            paper.setSize(file.getSize());
            paper.setName(name);
            paper.setFileType(fileType);
            if(fileType.equals("pdf")) {
                final File tempFile = File.createTempFile(name, "pdf");
                file.transferTo(tempFile);
                paper.setTotalPage(PdfUtils.getPdfPageSize(tempFile));
            }
            fileAnalysis.analysis(paper);
            mongoTemplate.save(paper);
        }
        return new Response(paper, "上传成功");
    }

    @PostMapping("/add")
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

    @PostMapping("/add_multiple")
    public Response<Paper> addPaperMultiple(@RequestBody PapersReq papersReq) {
        ArrayList<String> fileList = papersReq.getFileList();
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        for (String f : fileList) {
            Paper paper = new Paper();
            BeanUtils.copyProperties(papersReq, paper);
            String[] splitPath = f.split("/");
            String fileName = splitPath[splitPath.length - 1];
            String[] splitName = fileName.split("\\.");
            String name = splitName[0];
            String fileType = splitName[1];
            paper.setFile(f);
            paper.setName(name);
            paper.setFileType(fileType);
            Query query = Query.query(Criteria.where("name").is(paper.getName()));
            Paper _paper = mongoTemplate.findOne(query, Paper.class);
            if (_paper == null) {
                mongoTemplate.save(paper);
            }
        }
        return new Response(0, "success");
    }

    @PostMapping("/list")
    public Response<Grade> getPaperList() {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        List<Paper> list = mongoTemplate.findAll(Paper.class, "paper");
        return new Response(STAUTS_OK, list);
    }

    @PostMapping("/delete")
    public Response<Paper> deletePaper(@RequestBody ResponseWrap rw) {
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

    @PostMapping("/one")
    public Response<Paper> getOne(@RequestBody ResponseWrap rw) {
        String id = rw.getId();
        Paper result = MongoDBUtil.mongodbUtil.mongoTemplate.findById(id, Paper.class);
        if (result != null) {
            return new Response(STAUTS_OK, result);
        } else {
            return new Response(STAUTS_FAILED, "错误的id");
        }
    }

    @PostMapping("/page")
    public Response<Paper> getPaperPage(@RequestBody ResponseWrap rw) {

        HashMap query = new HashMap<String, Object>();
        PageModel page = MongoDBUtil.findSortPageCondition(
                Paper.class,
                "paper",
                rw.getQuery(),
                rw.getPageNo(),
                rw.getPageSize(),
                rw.getSort().getDirection(),
                rw.getSort().getValue(),
                rw.getKeyword());
        return new Response(STAUTS_OK, page);
    }

    @PostMapping("/edit")
    public Response<Paper> editGrade(@RequestBody Paper paper) throws IllegalAccessException {
        MongoDBUtil.updateMulti("_id", paper.getId(), CommonUtils.getObjectToMap(paper), "paper", 1);
        return new Response(0, "修改成功");
    }
}
