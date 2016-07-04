package com.example.administrator.test.exce;

/**
 * Created by glp on 2016/7/4.
 */

public class HttpMyException extends RuntimeException {

    public int mCode;

    public HttpMyException(int code,String msg){
        super(msg);
        mCode = code;
    }

    public int getCode(){
        return mCode;
    }
}
