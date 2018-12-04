package com.righthand.common.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by MJ on 2018. 8. 23..
 */

@Slf4j
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

    public static String eliminateHtmlTags(String text){
        final String regex = "<(\\\"[^\\\"]*\\\"|\\'[^\\']*\\'|[^\\'\\\">])*>";
        return text.replaceAll(regex,"");
    }
    public static String eliminateMarkdown(String text) {
        //  1. # -> ""
        text = text.replace("#", "");

        //  2. * [ ]
        text = text.replace("* [ ]", "");

        //  3. * -> ""
        text = text.replace("*", "");

        //  4. _ _ -> ""
        text = text.replace("_", "");

        //  5. ~~ ~~ -> ""
        text = text.replace("~~", "");

        //  6. > -> ""
        text = text.replace(">", "");

        //  7. * -> ""
        text = text.replace("*", "");

        //  8. ` ` -> ""
        text = text.replace("`", "");

        return text;
    }

    @CacheEvict(value = "findUserAndProfileCache", key = "#{userSeq}")
    public static void refreshCache(int userSeq) {
        log.info("[Cache][Refresing] userSeq : " + userSeq);
    }

}
