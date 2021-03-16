package top.kukechen.paperresourcebackend.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.kukechen.paperresourcebackend.errorHandle.BusinessException;
import top.kukechen.paperresourcebackend.errorHandle.NeedToLogin;


@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = NeedToLogin.class)
    public Response handleLogin(Throwable e) {
        e.printStackTrace();
        if(e instanceof NeedToLogin) {
            System.out.println("登录过期："+e.getMessage());
        }
        return ThrowableHandler.handle(e);
    }
}
