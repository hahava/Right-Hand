package com.righthand.common;

import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 *  현재 시간 정보를 가져온다.
 *
 *  현재 시간 정보를 타입 별로 가지고 온다.
 *
 */
@Service
public class GetNowTime {

    /**
     *  시간 정보를 String 형태로 리턴 한다.
     *
     * @param formatType
     * @return
     */
    public String getTimeByString(String formatType) {

        long time = System.currentTimeMillis();

        SimpleDateFormat dayTime = new SimpleDateFormat(formatType);
        dayTime.setTimeZone( TimeZone.getTimeZone( "UTC" ) );

        String nowTime = dayTime.format(new Date(time));

        return nowTime;
    }

    /**
     *
     * 시간 정보를 DataTime 으로 리턴한다.
     *
     * @return
     */
    public SimpleDateFormat getTimeByDate() {

        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        dayTime.setTimeZone( TimeZone.getTimeZone( "UTC" ) );

        return dayTime;
    }
}
