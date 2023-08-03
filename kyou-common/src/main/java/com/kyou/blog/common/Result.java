package com.kyou.blog.common;

import com.kyou.blog.common.emuration.HttpStatusCode;
import lombok.Data;

/**
 * @author CC
 * time 2023-05-26
 * description
 */
@Data
public class Result<T> {
    private T data;
    private String msg;
    private Integer code;

    public void setCode(Integer code) {
        this.code = code;
    }

    public Result(T data, String msg, Integer code) {
        this.data = data;
        this.msg = msg;
        this.code = code;
    }

    public Result(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }
    public static Result<String> sendMsg(String msg){
        return new Result<String>("",msg, HttpStatusCode.HTTP_OK.getCode());
    }
    public Result msg(String msg){
        this.setMsg(msg);
        return this;
    }
    public Result code(Integer code){
        this.setCode(code);
        return this;
    }
    public Result data(T data){
        this.setData(data);
        return this;
    }
    public static Result success(){
        return Result.success( "",HttpStatusCode.HTTP_OK.toString());
    }
    public static<T> Result<T> success(T data){
        return new Result<>(data, HttpStatusCode.HTTP_OK.toString(),HttpStatusCode.HTTP_OK.getCode());
    }
    public static<T> Result<T> success(T data,String msg){
        Result<T> r=new Result<>(data, msg,HttpStatusCode.HTTP_OK.getCode());
        return r;
    }
    public static<T> Result<T> serverError(String msg){
        Result<T> r=new Result<>(null, msg,HttpStatusCode.HTTP_SERVER_ERR.getCode());
        return r;
    }
    public static<T> Result<T> clientError(String msg){
        Result<T> r=new Result<>(null, msg,HttpStatusCode.HTTP_CLIENT_ERR.getCode());
        return r;
    }
    public static<T> Result<T> clientError(String msg,HttpStatusCode code){
        Result<T> r=new Result<>(null, msg,code.getCode());
        return r;
    }
}
