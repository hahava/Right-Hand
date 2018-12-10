package com.righthand.membership.customHandler;

import com.righthand.common.GetClientProfile;
import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;
import com.righthand.membership.config.ConfigMembership;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    MembershipService membershipService;

    @Autowired
    ConfigMembership configMembership;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 세션에 저장된 리퀘스트들을 가져온다.
    private RequestCache requestCache = new HttpSessionRequestCache();

    private String getUserInfoWhenSuccessLogin(){
        Map<String, Object> userInfo= null;
        try {
            userInfo = GetClientProfile.getUserInfo(membershipService);
        } catch (Exception e) {
            log.error("[getUserInfo][Exception]" + e.toString());
        }
        String jsonText = "{ \"authority : \" : " + String.valueOf(userInfo.get("authority")) + ", \"nickname\" : \"" + userInfo.get("nickname") + "\"}";
        return jsonText;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        logger.info("Login Success Handler");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        try {
            MembershipInfo membershipInfo = (MembershipInfo)authentication.getPrincipal();
            membershipInfo.setProfileSeq(membershipService.getProfileSeq(membershipInfo.getUserSeq()));
            membershipInfo.setNickname(membershipService.getProfileNickname(membershipInfo.getUserSeq()));

            // 코인과 파워를 가져온다
            Map map = membershipService.getRewardPowerAndCoin(membershipInfo.getProfileSeq());
            membershipInfo.setRhCoin((double)map.get("RH_COIN"));
            membershipInfo.setRewardPower((double)map.get("REWARD_POWER"));


            // 로그인 시, RhPower를 5개씩 준다.
            membershipService.updateRhPower(membershipInfo.getProfileSeq());

            writer.write(getUserInfoWhenSuccessLogin());
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }


        clearAuthenticationAttributes(request);
    }

}
