package top.kukechen.paperresourcebackend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.*;

public class JwtUtils {
    private static final String SIGN = "lsjdfshfi#%%#*nfhd";//将sign设置成全局变量

    public static String getToken(Map<String,String> map){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,7);//定义过期时间
        Date date = calendar.getTime();

        JWTCreator.Builder builder = JWT.create();
        map.forEach((k,v)->{
            builder.withClaim(k,v);//使用map的forEach()方法（lambda表达式），动态设置payload
        });
        String token = builder.withExpiresAt(date)//为token设置过期时间
                .sign(Algorithm.HMAC256(SIGN));//为token设置签名及密钥

        return token;
    }

    public static DecodedJWT verifyToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        return decodedJWT;
    }
}