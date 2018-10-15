package com.righthand.common.dto.res;

import com.righthand.common.type.ReturnType;
import lombok.Data;

@Data
public class HttpDefaultRes<T> {
    public  static final String RETURN_CODE = "code";
    public  static final String RETURN_MESSAGE = "message";
    public  static final String RETURN_DATA = "data";

    public HttpDefaultRes(){super();}
    public HttpDefaultRes(ReturnType returnType){this.setReturnCode(returnType);}
    public HttpDefaultRes(ReturnType returnType, T result){
        this.setReturnCode(returnType);
        this.setData(result);
    }

    private Integer code;
    private String message;
    private T data;

    public void setReturnCode(ReturnType ret){
        this.setCode(ret.getValue());
        this.setMessage(ret.getMessage());
    }


    public void setData(T obj){
        this.data = obj;
    }

//    public void setData(JSONObject resultJson) {
//        this.data = (T)resultJson;
//    }
}