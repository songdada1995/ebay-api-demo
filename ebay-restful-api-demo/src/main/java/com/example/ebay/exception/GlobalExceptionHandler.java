package com.example.ebay.exception;

import com.example.ebay.constants.ResultCode;
import com.example.ebay.uitls.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author songbo
 * @Date 2021/5/8 14:43
 * @Version 1.0
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = BasicException.class)
    public ResponseUtils.ResponseVO basicExceptionHandler(BasicException e) {
        ResponseUtils.ResponseVO result = ResponseUtils.newResponse();
        result.failed(e.getErrorCode(), e.getMessage());
        result.setData(e.getData());

        if (e.getOriginException() != null) {
            result.setExceptionStackTrace(BasicException.stackTraceToString(e.getOriginException()));
        }

        log.info(e.getMessage());
        return result;
    }

    @ResponseBody
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseUtils.ResponseVO noHandlerFoundExceptionHandler(HttpServletRequest req) {
        ResponseUtils.ResponseVO result = ResponseUtils.newResponse();
        String msg = "接口 [" + req.getRequestURI() + "] 不存在";
        result.failed(ResultCode.Common.NOT_FOUND.code, msg);

        log.warn(msg);
        return result;
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseUtils.ResponseVO exceptionHandler(HttpServletRequest req, HandlerMethod handlerMethod, Exception e) {
        ResponseUtils.ResponseVO result = ResponseUtils.newResponse();
        result.failed(ResultCode.Common.INTERNAL_SERVER_ERROR.code, "接口 [" + req.getRequestURI() + "] 内部错误，请联系管理员");
        result.setExceptionStackTrace(BasicException.stackTraceToString(e));

        String message;
        if (handlerMethod instanceof HandlerMethod) {
            message = String.format(
                    "接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                    req.getRequestURI(),
                    handlerMethod.getBean().getClass().getName(),
                    handlerMethod.getMethod().getName(),
                    e.getMessage()
            );
        } else {
            message = e.getMessage();
        }

        log.error(message, e);
        return result;
    }

}
