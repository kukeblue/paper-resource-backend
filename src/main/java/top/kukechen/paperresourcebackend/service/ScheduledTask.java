package top.kukechen.paperresourcebackend.service;
import com.mongodb.client.result.UpdateResult;
import lombok.SneakyThrows;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.kukechen.paperresourcebackend.model.Grade;
import top.kukechen.paperresourcebackend.model.GradeStep;
import top.kukechen.paperresourcebackend.model.Paper;
import top.kukechen.paperresourcebackend.utils.CommonUtils;
import top.kukechen.paperresourcebackend.utils.HttpClientUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledTask {

    // 10分钟执行一次
    @Scheduled(fixedRate = 600000)
    public void createPreViewFileScheduledTask(){
        System.out.println("任务执行时间：" + LocalDateTime.now());
        HashMap map = new HashMap();
        map.put("createPreviewFile", false);
        List<Paper> list = (List<Paper>) MongoDBUtil.findSortByParam(Paper.class, "paper", map, "name", Sort.Direction.ASC);
        list.forEach(paper -> {
            String fileUrl = paper.getFile();
            try {
                String base64encodedString = Base64.getEncoder().encodeToString(fileUrl.getBytes("utf-8"));
                String urlencodeString = java.net.URLDecoder.decode(base64encodedString, "UTF-8" );
                String path = "http://103.100.210.203:8012/onlinePreview?url=" + urlencodeString;
                System.out.println("生成：" + paper.getName() + "文件， 地址为" + path);
                String res = HttpClientUtils.doGet(path);
                if(res != null && !res.isEmpty()) {
                    String folderName = fileUrl.replace("http://kuke-static.kukechen.top/", "").replace(".pdf", "");
                    File file = new File("/opt/paper_preview/" + folderName);
                    file.mkdirs();
                    CommonUtils.copyFolder("/opt/paper/" +  folderName , "/opt/paper_preview/" + folderName);
                    MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
                    Criteria criteria = Criteria.where("_id").is(paper.getId());
                    Query query = Query.query(criteria);
                    Update update = new Update();
                    update.set("createPreviewFile", true);
                    mongoTemplate.updateFirst(query, update, Paper.class);
                }
                TimeUnit.SECONDS.sleep(10);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

}
