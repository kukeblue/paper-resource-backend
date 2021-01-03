package top.kukechen.paperresourcebackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.units.FileUpload;
import top.kukechen.paperresourcebackend.units.PassToken;
import java.io.File;

@RestController
@RequestMapping("/paper")
public class PaperController {

    private static Logger logger = LoggerFactory.getLogger(PaperController.class);

    @PassToken
    @PostMapping("/upload")
    public Response standardServletMultipartResolver(@RequestParam(name = "file") MultipartFile file) {
        logger.info("检测到文件上传，上传启动...");
        File directory = FileUpload.buildDirectory();
        FileUpload.upload(file, directory);
        return new Response(Response.STAUTS_OK, "文件上传成功");
    }
}
