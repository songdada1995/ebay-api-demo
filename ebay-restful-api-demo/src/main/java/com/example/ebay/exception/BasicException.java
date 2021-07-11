package com.example.ebay.exception;

import com.example.ebay.constants.ResultCode;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BasicException extends RuntimeException {

    private String errorMsg;
    private Integer errorCode;
    private Object data;
    private Exception originException;

    public String getMessage() {
        if (StringUtils.isBlank(this.errorMsg) && originException != null) {
            return originException.getMessage();
        }
        return this.getErrorMsg();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        if (errorCode != null) {
            this.errorCode = errorCode;
        }
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Exception getOriginException() {
        return originException;
    }

    public void setOriginException(Exception originException) {
        this.originException = originException;
    }

    public BasicException(String msg) {
        super(msg);
        this.errorCode = ResultCode.Common.UNKNOWN_ERROR.code;
        this.setErrorMsg(msg);
    }

    public static void throwException(String errorMsg, Integer errorCode) {
        throwException(errorMsg, errorCode, null, null);
    }

    public static void throwException(String errorMsg, Integer errorCode, Object data) {
        throwException(errorMsg, errorCode, data, null);
    }

    public static void throwException(String errorMsg, Integer errorCode, Object data, Exception originException) {
        BasicException e = new BasicException(errorMsg);
        e.setErrorCode(errorCode);
        e.setData(data);
        e.setOriginException(originException);
        throw e;
    }

    public static String stackTraceToString(Exception e) {
        if (e == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                PrintStream writer = new PrintStream(out);
        ) {
            if (e != null) {
                e.printStackTrace(writer);
                writer.flush();
                stringBuilder.append(new String(out.toByteArray()));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
