package com.righthand.common;

import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

/**
 * 데이터의 존재 유무를 확인 한다.
 *
 */
@Service
public class CheckData {

    /**
     * obj mapping 함수
     *
     * @param obj
     * @return
     */
    public boolean isExist(Object obj) {
        if(obj == null || obj.toString().isEmpty())
        {
            return false;
        }

        return true;
    }

    /**
     * str mapping 함수
     *
     * @param str
     * @return
     */
    public boolean isExist(String str) {
        if(str == null || str.toString().isEmpty())
        {
            return false;
        }

        return true;
    }


    /**
     * obj mapping 함수
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj instanceof String) return obj == null || "".equals(obj.toString().trim());
        else if (obj instanceof List) return obj == null || ((List<?>) obj).isEmpty();
        else if (obj instanceof Map) return obj == null || ((Map<?, ?>) obj).isEmpty();
        else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
        else return obj == null;
    }

    /**
     * obj mapping 함수
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

}
