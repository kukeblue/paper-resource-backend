package top.kukechen.paperresourcebackend.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.kukechen.paperresourcebackend.errorHandle.BusinessException;


@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 默认返回HttpStatus.OK, 这里不是必须的，如果返回其他状态码才有必要加上
     * @param e
     * @return 封装后的应答对象
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({Throwable.class})
    public Response handle(Throwable e) {
        e.printStackTrace();
        if(e instanceof BusinessException) {
            System.out.println("业务异常："+e.getMessage());
//            BusinessException businessException = (BusinessException)e;
        }
        return ThrowableHandler.handle(e);
    }


}
