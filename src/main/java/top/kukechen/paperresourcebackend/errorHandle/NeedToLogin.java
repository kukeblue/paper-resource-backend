package top.kukechen.paperresourcebackend.errorHandle;

import top.kukechen.paperresourcebackend.units.ResultEnum;

import static top.kukechen.paperresourcebackend.units.ResultEnum.NO_LOGIN;

public class NeedToLogin  extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code;  //错误码

    public NeedToLogin() {
        super(NO_LOGIN.getMsg());
        this.code = NO_LOGIN.getCode();
    }

    public NeedToLogin(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}