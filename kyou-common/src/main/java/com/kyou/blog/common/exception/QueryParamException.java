package com.kyou.blog.common.exception;

import com.kyou.blog.common.constant.MsgConstant;

/**
 * @author CC
 * time 2023-07-13
 * description 请求参数异常
 */
public class QueryParamException extends RuntimeException{

    public QueryParamException() {
        super(MsgConstant.QUERY_PARAM_ERR);
    }

    public QueryParamException(String message) {
        super(message);
    }

    public QueryParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryParamException(Throwable cause) {
        super(cause);
    }

}
