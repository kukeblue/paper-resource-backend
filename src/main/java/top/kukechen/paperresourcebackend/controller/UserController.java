package top.kukechen.paperresourcebackend.controller;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kukechen.paperresourcebackend.model.User;
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.service.MongoDBUtil;
import top.kukechen.paperresourcebackend.units.PassToken;
import org.jasypt.util.password.BasicPasswordEncryptor;


@RestController
public class UserController {

    @PassToken
    @GetMapping("/user/register")
    public Response register() {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Query query = Query.query(Criteria.where("username").is("admin"));
        User user = mongoTemplate.findOne(query, User.class);
        Response response = new Response<User>();
        if(user == null) {
            String password = "123456";
            BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
            //加密密码
            String encryptedPassword = encryptor.encryptPassword(password);
            //检查密码：正确
//          System.out.println(encryptor.checkPassword("MyPassword", encryptedPassword));
            //检查密码：错误
//          System.out.println(encryptor.checkPassword("myPassword", encryptedPassword));
            user = new User();
            user.setUsername("admin");
            user.setPassword(encryptedPassword);
            User res = mongoTemplate.save(user);
        }
        response.setResult(user.getUserInfo());
        return response;
    }
}
