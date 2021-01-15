package top.kukechen.paperresourcebackend.units;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class FileUpload {
    public static FileUpload fileUpload;

    public FileUpload() throws IOException {
    }

    @PostConstruct
    public void init() {
        fileUpload = this;
    }

    @Value("${upload.path}")
    public String PATH;

    public static File buildDirectory() {
        File directory = new File(fileUpload.PATH);
        if (!directory.exists() || !directory.isDirectory()) {
            directory.mkdirs();
        }
        return directory;
    }

    public static File upload(MultipartFile multipartFile, File directory) {
        File file = new File(fileUpload.PATH + multipartFile.getOriginalFilename());
        String filename = multipartFile.getOriginalFilename();
        if (file.exists()) {
            filename = System.currentTimeMillis() + filename;
        }
        try {
            File newFile = new File(directory, filename);
            multipartFile.transferTo(newFile);
            uploadPriviewServer(newFile);
            return newFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void uploadPriviewServer(File file) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
                .build();
        Request request = new Request.Builder()
                .url("http://api-paperfile.kukechen.top/fileUpload")
                .post(multipartBody)
                .build();
        Response response = client.newCall(request).execute();
    }
}
