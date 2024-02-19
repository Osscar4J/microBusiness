package com.zhao.micro.exception;

import com.zhao.micro.respvo.BaseResponse;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = Logger.getLogger(getClass());

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseResponse<?> jsonErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        BaseResponse<?> resp = BaseResponse.getError();
        resp.setMsg(e.getMessage());
        if (e instanceof BusinessException) {
            resp.setCode(((BusinessException) e).getCode());
        } else {
            resp.setCode(500);
        }
        StackTraceElement firstTrace = e.getStackTrace()[0];
        logger.error("================== Exception ==================");
        logger.error("触发类：" + firstTrace.getClassName());
        logger.error("方法：L" + firstTrace.getLineNumber() + ":" + firstTrace.getMethodName());
        logger.error("信息：" + e.getMessage());
        return resp;
    }
}
