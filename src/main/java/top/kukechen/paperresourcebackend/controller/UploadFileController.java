package top.kukechen.paperresourcebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.kukechen.paperresourcebackend.qiniu.UploadFile;
import top.kukechen.paperresourcebackend.qiniu.UploadFileQiniu;
import top.kukechen.paperresourcebackend.qiniu.UploadProperties;
import top.kukechen.paperresourcebackend.units.PassToken;

@RestController
@RequestMapping("/upload")
public class UploadFileController {
    @Autowired
    UploadProperties uploadProperties;

    @PostMapping("/img")
    @PassToken
    public String uploadFileYun(MultipartFile file) {
        // 上传到七牛云
        UploadFile uploadFile = new UploadFileQiniu(uploadProperties.getQiniu());
        return uploadFile.uploadFile(file);
    }
}