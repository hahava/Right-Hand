package com.righthand.common.dto.res;

import com.righthand.common.type.ReturnType;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler<T> extends HttpDefaultRes<T> {

    public static ResponseEntity<?> getObjectResponse(ReturnType rtn, Object obj, HttpStatus status) {
        //obj.getClass()

//        JSONObject obj = new JSONObject();
//        obj.put("code", rtn.getValue());
//        obj.put("msg", rtn.getMessage());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity<>(obj, responseHeaders, status);
    }

    public static ResponseEntity<?> getHttpResponse(Object resultObj, HttpStatus status) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity<>(resultObj, responseHeaders, status);
    }

    public static ResponseEntity<JSONObject> getJSONResponse(ReturnType rtn, Object req, HttpStatus status) {


        JSONObject obj = new JSONObject();
        obj.put("code", rtn.getValue());
        obj.put("msg", rtn.getMessage());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity<JSONObject>(obj, responseHeaders, status);
    }

    public static ResponseEntity<JSONObject> getJSONResponse(JSONObject resultObj, HttpStatus status) {

        JSONObject obj = new JSONObject();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity<JSONObject>(resultObj, responseHeaders, status);
    }

}