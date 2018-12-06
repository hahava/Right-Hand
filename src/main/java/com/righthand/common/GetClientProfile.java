package com.righthand.common;

import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;

import java.util.HashMap;
import java.util.Map;

/*
*  생성자 : DANNY
* */

public class GetClientProfile {

    public static Map<String, Object> getUserInfo(MembershipService membershipService) throws Exception{
        Map<String, Object> map = new HashMap<>();
        try {
            MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
            map.put("authority", membershipInfo.getAuthoritiesLevel());
            map.put("nickname", membershipInfo.getNickname());
            map.put("profileSeq",membershipInfo.getProfileSeq());
        } catch (Exception e) {
            map.put("authority", 0);
            map.put("nickname", null);
            map.put("profileSeq", null);
        }
        return map;
    }
}
