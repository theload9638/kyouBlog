package com.kyou.blog.common.emuration;


/**
 * @author CC
 * time 2023-05-08
 * description
 */
public enum HttpStatusCode {
    HTTP_OK(200,"成功"),
    HTTP_SERVER_ERR(500,"服务器异常"),
    HTTP_NO_AUTHENTICATION(403,"身份未认证"),
    HTTP_NO_AUTHORIZATION(401,"未授权"),
    HTTP_TOKEN_EXPIRE(406,"认证过期"),
    NOT_FOUND(404,"资源未找到"),
    HTTP_CLIENT_ERR(400,"客户端异常");
    private Integer code;
    private String msg;

    HttpStatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return msg;
    }
}
