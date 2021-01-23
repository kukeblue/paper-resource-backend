package top.kukechen.paperresourcebackend.utils;

public enum ResultEnum {
    UNKONW_ERROR(-1,"未知错误"),
    SUCCESS(0,"成功"),
    ERROR(1,"失败"),
    NO_LOGIN(401,"请先登录"),;

    private Integer code;
    private String msg;

    ResultEnum(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
