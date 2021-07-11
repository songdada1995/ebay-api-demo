package com.example.ebay.uitls;

import com.example.ebay.constants.ResultCode;
import com.example.ebay.exception.BasicException;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ResponseUtils {

    public static ResponseVO newResponse() {
        return new ResponseVO();
    }

    public static class ResponseVO {
        private boolean success = true;
        private String message;
        private Object data;
        private Integer errorCode;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String exceptionStackTrace;

        public String getExceptionStackTrace() {
            return exceptionStackTrace;
        }

        public void setExceptionStackTrace(String exceptionStackTrace) {
            this.exceptionStackTrace = exceptionStackTrace;
        }

        public ResponseVO succeed(Object data) {
            this.setSuccess(true);
            this.setData(data);
            return this;
        }

        public ResponseVO succeedWithList(Long total, List<? extends Object> rows) {
            this.setSuccess(true);
            this.setData(FastCollections.map("total", total, "rows", rows));
            return this;
        }

        public ResponseVO succeedWithList(Long total, List<? extends Object> rows, Object sumData) {
            this.setSuccess(true);
            this.setData(FastCollections.map("total", total, "rows", rows, "sumData", sumData));
            return this;
        }

        public ResponseVO succeed() {
            this.setSuccess(true);
            return this;
        }

        public ResponseVO failed(BasicException e) {
            return failed(this.getData(), e);
        }

        public ResponseVO failed(Object data, Exception e) {
            this.setSuccess(false);
            this.setData(data);
            if (e != null) {
                this.setMessage(e.getMessage());
                if (e instanceof BasicException) {
                    BasicException basicException = (BasicException) e;
                    if (basicException.getData() != null) {
                        this.setData(basicException.getData());
                    }
                    this.setErrorCode(basicException.getErrorCode());
                    this.setExceptionStackTrace(BasicException.stackTraceToString(basicException.getOriginException()));
                } else {
                    this.setErrorCode(ResultCode.Common.INTERNAL_SERVER_ERROR.code);
                    this.setExceptionStackTrace(BasicException.stackTraceToString(e));
                }
            }
            return this;
        }

        public ResponseVO failed(Integer errorCode, String message) {
            if (errorCode == null) {
                errorCode = ResultCode.Common.UNKNOWN_ERROR.code;
            }
            this.setSuccess(false);
            this.setErrorCode(errorCode);
            this.setMessage(message);
            return this;
        }

        public ResponseVO failed(Integer errorCode, String message, Object data) {
            if (errorCode == null) {
                errorCode = ResultCode.Common.UNKNOWN_ERROR.code;
            }
            this.setSuccess(false);
            this.setErrorCode(errorCode);
            this.setMessage(message);
            this.setData(data);
            return this;
        }

        public ResponseVO message(String message) {
            this.setMessage(message);
            return this;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Integer getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(Integer errorCode) {
            this.errorCode = errorCode;
        }
    }

    /**
     * 变成导出的out对象
     *
     * @param response
     * @param fileName
     */
    public static void becomeExportOut(HttpServletResponse response, String fileName) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
