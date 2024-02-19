package com.zhao.micro.respvo;

/**
 * 统一响应基类
 *
 * @param <T>
 * @author Administrator
 */
public class BaseResponse<T> {

    private int code;
    private T content;
    private String msg;

    private BaseResponse() {
    }

    @SuppressWarnings("rawtypes")
    private static BaseResponse SUCCESS = new BaseResponse<>(0, "成功");
    private static BaseResponse<?> ERROR = new BaseResponse<>();

    private BaseResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static BaseResponse<?> getError() {
        return ERROR;
    }

    public static BaseResponse<?> ERROR(String msg) {
        ERROR.code = 500;
        ERROR.msg = msg;
        return ERROR;
    }

    public static <T> BaseResponse<T> SUCCESS(T t) {
        SUCCESS.code = 0;
        SUCCESS.content = t;
        return SUCCESS;
    }

    public static <T> BaseResponse<T> SUCCESS(int code, T t) {
        SUCCESS.code = code;
        SUCCESS.content = t;
        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    public static <T> BaseResponse<T> SUCCESS() {
        SUCCESS.code = 0;
        SUCCESS.content = null;
        return SUCCESS;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
