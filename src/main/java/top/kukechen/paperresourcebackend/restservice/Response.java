package top.kukechen.paperresourcebackend.restservice;


import lombok.Data;
import top.kukechen.paperresourcebackend.service.PageModel;

import java.util.List;

@Data
public final class Response<T> {

    public Response() {
    }

    public Response(int status, T t) {
        this.status = status;
        this.result = t;
    }

    public Response(int status, String errorMsg) {
        this.status = status;
        this.errorMsg = errorMsg;
    }

    public Response(int status, T t, String message) {
        this.status = status;
        this.message = message;
        this.result = t;
    }
    public Response(T t, String message) {
        this.status = STAUTS_OK;
        this.result = t;
    }
    public Response(int status, List<T> list) {
        this.status = status;
        this.list = list;
    }

    public Response(int status, PageModel page) {
        this.status = status;
        this.page = page;
    }

    /** 应答状态-成功 */
    public static final int STAUTS_OK = 0;

    /** 应答状态-失败 */
    public static final int STAUTS_FAILED = -1;

    /** 默认错误码 */
    public static final String DEFAULT_ERROR_CODE = "000000";

    /** 应答状态 */
    private int status = STAUTS_OK;

    /** 应答错误码，有错误时返回 */
    private String errorCode = DEFAULT_ERROR_CODE;

    /** 应答错误消息，有错误时返回 */
    private String errorMsg;

    private String message;
//
//    /** 应答错误码，有错误时返回 */
//    private String errorStack;

    /** 应答结果，存放业务应答内容 */
    private T result;

    private List<T> list;

    private PageModel page;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

//    public String getErrorStack() {
//        return errorStack;
//    }
//
//    public void setErrorStack(String errorStack) {
//        this.errorStack = errorStack;
//    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
