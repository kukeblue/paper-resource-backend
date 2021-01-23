package top.kukechen.paperresourcebackend.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.kukechen.paperresourcebackend.model.Paper;
import top.kukechen.paperresourcebackend.model.WxResponse;
import top.kukechen.paperresourcebackend.model.WxUser;
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.utils.HttpClientUtils;
import top.kukechen.paperresourcebackend.utils.JsonUtils;
import top.kukechen.paperresourcebackend.utils.PassToken;

import java.util.HashMap;
import java.util.Map;

import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_FAILED;
import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_OK;

@RestController
@RequestMapping("/wxUser")

public class WxUserController {
    private  String appId = "wx4f594f42b6ca60d8";
    private String secret = "1a085e09f9e33c687ffe0ce6a031e3e6";

    @PassToken
    @RequestMapping("/login")
    @ResponseBody
    public Response<WxUser> login(String code)
    {
        System.out.println("wxlogin - code: " + code);
//		https://api.weixin.qq.com/sns/jscode2session?
//				appid=APPID&
//				secret=SECRET&
//				js_code=JSCODE&
//				grant_type=authorization_code
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
}
