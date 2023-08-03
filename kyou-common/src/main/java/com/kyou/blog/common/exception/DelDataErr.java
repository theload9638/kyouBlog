package com.kyou.blog.common.exception;

import com.kyou.blog.common.constant.MsgConstant;

/**
 * @author CC
 * time 2023-07-13
 * description
 */
public class DelDataErr extends RuntimeException{
    public DelDataErr() {
        super(MsgConstant.DEL_DATA_ERR);
    }

    public DelDataErr(String message) {
        super(message);
    }

    public DelDataErr(String message, Throwable cause) {
        super(message, cause);
    }

    public DelDataErr(Throwable cause) {
        super(cause);
    }
}
