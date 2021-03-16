package top.kukechen.paperresourcebackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import top.kukechen.paperresourcebackend.model.*;
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.restservice.ResponseWrap;
import top.kukechen.paperresourcebackend.service.MongoDBUtil;
import top.kukechen.paperresourcebackend.utils.HttpClientUtils;
import top.kukechen.paperresourcebackend.utils.JsonUtils;
import top.kukechen.paperresourcebackend.utils.JwtUtils;
import top.kukechen.paperresourcebackend.utils.PassToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_FAILED;
import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_OK;

@RestController
@RequestMapping("/wx")

public class WxController {
    private  String appId = "wx4f594f42b6ca60d8";
    private String secret = "1a085e09f9e33c687ffe0ce6a031e3e6";
    private static Logger logger = LoggerFactory.getLogger(WxController.class);

    @PassToken
    @RequestMapping("/login")
    @ResponseBody
    public Response<ResponseUser> login(String code)
    {
        System.out.println("wxlogin - code: " + code);
        if(code == null) {
            return new Response<ResponseUser>(STAUTS_FAILED,"登录缺少code");
        }
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> param = new HashMap<>();
        param.put("appid", appId);
        param.put("secret", secret);
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        String wxResult = HttpClientUtils.doGet(url, param);
        WxResponse res;
        try {
            res = JsonUtils.jsonToPojo(wxResult, WxResponse.class);
        }
        catch (Exception e){
            return new Response<ResponseUser>(STAUTS_FAILED,"登陆失败");
        }
        String openid = res.getOpenid();
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Query query = Query.query(Criteria.where("openid").is(openid));
        WxUser wxuser = mongoTemplate.findOne(query, WxUser.class);
        if(wxuser == null) {
            wxuser  = new WxUser(openid);
            wxuser = MongoDBUtil.mongodbUtil.mongoTemplate.save(wxuser);
        }
        Map userData = new HashMap<String,String>();
        userData.put("userId", wxuser.getId());
        String newToken = JwtUtils.getToken(userData);
        ResponseUser responseUser = new ResponseUser();
        responseUser.setToken(newToken);
        responseUser.setWxUser(wxuser);
        return new Response<ResponseUser>(STAUTS_OK, responseUser);
    }
}
