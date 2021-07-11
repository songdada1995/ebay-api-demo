package com.example.ebay.constants;

public interface ResultCode {

    //1001xxx
    enum Common {
        UNKNOWN_ERROR(1001000),             //未知错误
        NOT_FOUND(1001001),                 //资源不存在
        ILLEGAL_ARGUMENT(1001002),          //参数非法
        INTERNAL_SERVER_ERROR(1001500),     //服务器内部错误
        NOT_MET_CONDITION(1001202),         //不符合逻辑执行条件
        ;
        public int code;

        Common(int code) {
            this.code = code;
        }
    }

    //1000xxx
    enum User {
        NOT_SIGNIN(1000000),           //未登录或者回话过期

        ;
        public int code;

        User(int code) {
            this.code = code;
        }
    }

}
