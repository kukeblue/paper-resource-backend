package top.kukechen.paperresourcebackend.controller;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import top.kukechen.paperresourcebackend.model.ResponseUser;
import top.kukechen.paperresourcebackend.model.User;
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.service.MongoDBUtil;
import top.kukechen.paperresourcebackend.units.JwtUtils;
import top.kukechen.paperresourcebackend.units.PassToken;


@RestController
public class UserController {

    @PassToken
    @PostMapping ("/user/register")
    public Response register(@RequestBody User user) {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Query query = Query.query(Criteria.where("username").is(user.getUsername()));
        User db_user = mongoTemplate.findOne(query, User.class);
        Response response = new Response<User>();
        if(db_user == null) {
            User res = mongoTemplate.save(user);
        }
        response.setResult(user.getUserInfo());
        return response;
    }

    @PassToken
    @PostMapping ("/user/login")
    public Response login( @RequestBody(required = false) User loginUser){
        System.out.println("User:" + loginUser);
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Query query = Query.query(Criteria.where("username").is(loginUser.getUsername()));
        User user = mongoTemplate.findOne(query, User.class);
        System.out.println("dbUser:"+loginUser);
        if (user == null) {
            return new Response(Response.STAUTS_FAILED, "用户名不存在");
        } else {
            if (!loginUser.getPassword().equals(user.getPassword()) ) {
                return new Response(Response.STAUTS_FAILED, "密码错误");
            }
            else{
                String newToken = JwtUtils.createToken(user.getId(), user.getUsername(), user.getUsername());
                ResponseUser responseUser = new ResponseUser(loginUser.getUsername(),newToken);
                return new Response(Response.STAUTS_OK,responseUser,"登陆成功");
            }
        }
    }
}
