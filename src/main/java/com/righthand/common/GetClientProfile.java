package com.righthand.common;

import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;

import java.util.HashMap;
import java.util.Map;

public class GetClientProfile {

    public static Map<String, Object> getUserInfo(MembershipService membershipService){
        Map<String, Object> map = new HashMap<>();
        try {
            MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
            map.put("authority", membershipInfo.getAuthoritiesLevel());
            map.put("nickname", membershipInfo.getNickname());
        } catch (Exception e) {
            map.put("authority", 0);
            map.put("nickname", null);
        }
        return map;
    }
}
