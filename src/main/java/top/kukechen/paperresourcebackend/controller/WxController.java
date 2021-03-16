package top.kukechen.paperresourcebackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import top.kukechen.paperresourcebackend.model.Paper;
import top.kukechen.paperresourcebackend.model.Tag;
import top.kukechen.paperresourcebackend.model.WxResponse;
import top.kukechen.paperresourcebackend.model.WxUser;
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.restservice.ResponseWrap;
import top.kukechen.paperresourcebackend.service.MongoDBUtil;
import top.kukechen.paperresourcebackend.utils.HttpClientUtils;
import top.kukechen.paperresourcebackend.utils.JsonUtils;
import top.kukechen.paperresourcebackend.utils.PassToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_FAILED;
import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_OK;

@RestController
@RequestMapping("/wx")

public class WxUserController {
    private  String appId = "wx4f594f42b6ca60d8";
    private String secret = "1a085e09f9e33c687ffe0ce6a031e3e6";
    private static Logger logger = LoggerFactory.getLogger(WxUserController.class);

    @PassToken
    @RequestMapping("/login")
    @ResponseBody
    public Response<WxUser> login(String code)
    {
        System.out.println("wxlogin - code: " + code);
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> param = new HashMap<>();
        param.put("appid", appId);
        param.put("secret", secret);
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        String wxResult = HttpClientUtils.doGet(url, param);
        System.out.println(wxResult);
        WxResponse res;
        try {
            res = JsonUtils.jsonToPojo(wxResult, WxResponse.class);
        }
        catch (Exception e){
            return new Response<WxUser>(STAUTS_FAILED,"登陆失败");
        }
        WxUser user  = new WxUser(res.getOpenid());
        return new Response<WxUser>(STAUTS_OK,user );

    }
    @PassToken
    @PostMapping("/add")
    public Response<WxUser> add(@RequestBody WxUser wxUser){
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Query query = Query.query(Criteria.where("nickName").is(wxUser.getNickName()));
        WxUser _wxUser = mongoTemplate.findOne(query, WxUser.class);
        if (_wxUser != null) {
            return new Response<WxUser>(STAUTS_FAILED, "微信用户已存在");
        }
        mongoTemplate.save(wxUser);
        return new Response(0, wxUser);
    }   

//    @PostMapping("/list")
//    @PassToken
//    public Response<Tag> getWxUserList() {
//        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
//        List<WxUser> list = mongoTemplate.findAll(WxUser.class, "wx_user");
//        return new Response(STAUTS_OK, list);
//    }
//
//    @PostMapping("/delete")
//    @PassToken
//    public Response<Tag> deleteWxUser(@RequestBody ResponseWrap rw) {
//        String id = rw.getId();
//        logger.info("开始删除微信用户 " + id);
//        int result = MongoDBUtil.removeById(id, "wx_user");
//        logger.info("删除标签成功: id为" + id + " 删除结果 - result: " + result);
//        if (result == 1) {
//            return new Response(STAUTS_OK, "删除成功");
//        } else {
//            return new Response(STAUTS_FAILED, "删除失败");
//        }
//    }
}
