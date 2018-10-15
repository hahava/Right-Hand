package com.righthand.common.util;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by MJ on 2018. 8. 23..
 */

public class ConvertUtil {
    public static Map<String, Object> convertObjectToMap(Object vo) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> params = objectMapper.convertValue(vo, Map.class);

        return params;
    }

    public static Object convertMapToObject(Map<String,Object> map,Object obj){
        String keyAttribute = null;
        String setMethodString = "set";
        String methodString = null;
        Iterator itr = map.keySet().iterator();

        while(itr.hasNext()){
            keyAttribute = (String) itr.next();
            methodString = setMethodString+keyAttribute.substring(0,1).toUpperCase()+keyAttribute.substring(1);
            Method[] methods = obj.getClass().getDeclaredMethods();
            for(int i=0;i<methods.length;i++){
                if(methodString.equals(methods[i].getName())){
                    try{
                        methods[i].invoke(obj, map.get(keyAttribute));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }

}
