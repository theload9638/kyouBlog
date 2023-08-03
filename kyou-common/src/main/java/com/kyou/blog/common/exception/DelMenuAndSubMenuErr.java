package com.kyou.blog.common.exception;

import com.kyou.blog.common.constant.MsgConstant;

/**
 * @author CC
 * time 2023-07-13
 * description
 */
public class DelMenuAndSubMenuErr extends RuntimeException{
    public DelMenuAndSubMenuErr() {
        super(MsgConstant.DEL_MENU_WITH_SUB_ERR);
    }

    public DelMenuAndSubMenuErr(String message) {
        super(message);
    }

    public DelMenuAndSubMenuErr(String message, Throwable cause) {
        super(message, cause);
    }

    public DelMenuAndSubMenuErr(Throwable cause) {
        super(cause);
    }
}
