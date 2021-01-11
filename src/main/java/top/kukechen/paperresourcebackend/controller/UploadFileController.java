package top.kukechen.paperresourcebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.kukechen.paperresourcebackend.model.Paper;
import top.kukechen.paperresourcebackend.qiniu.UploadFile;
import top.kukechen.paperresourcebackend.qiniu.UploadFileQiniu;
import top.kukechen.paperresourcebackend.qiniu.UploadProperties;
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.units.PassToken;

@RestController
@RequestMapping("/upload")
public class UploadFileController {
    @Autowired
    UploadProperties uploadProperties;

    @PostMapping("/file")
    @PassToken
    public Response<String> uploadFileYun(MultipartFile file) {
        // 上传到七牛云
        UploadFile uploadFile = new UploadFileQiniu(uploadProperties.getQiniu());
        String  url = uploadFile.uploadFile(file);
        return new Response(0, url, "上传成功");
    }
}
