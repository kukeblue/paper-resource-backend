package top.kukechen.paperresourcebackend.qiniu;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFile {
    String uploadFile(MultipartFile file);
}
