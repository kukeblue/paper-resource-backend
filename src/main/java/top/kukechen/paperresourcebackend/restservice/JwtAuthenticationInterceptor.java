package top.kukechen.paperresourcebackend.restservice;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.kukechen.paperresourcebackend.controller.GradeController;
import top.kukechen.paperresourcebackend.errorHandle.NeedToLogin;
import top.kukechen.paperresourcebackend.units.JwtUtils;
import top.kukechen.paperresourcebackend.units.PassToken;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


public class JwtAuthenticationInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(JwtAuthenticationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
        String token = httpServletRequest.getHeader("auth");
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        else {
            System.out.println("被jwt拦截需要验证");
            // 执行认证
            if (token == null) {
                throw new NeedToLogin();
            }
            DecodedJWT decodedJWT = JwtUtils.verifyToken(token);
//            String userId = decodedJWT.getClaim("userId").asString();
            return true;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}