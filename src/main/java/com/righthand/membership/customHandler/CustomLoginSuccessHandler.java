package com.righthand.membership.customHandler;

import com.righthand.common.type.ReturnType;
import com.righthand.membership.config.ConfigMembership;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
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

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    MembershipService membershipService;

    @Autowired
    ConfigMembership configMembership;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 세션에 저장된 리퀘스트들을 가져온다.
    private RequestCache requestCache = new HttpSessionRequestCache();

    private static String getAuthoritiesString(int authoritiesLevel, ConfigMembership configMembership){
        int adminNo = configMembership.getSelectAuthorityAdminNo() +
                configMembership.getSelectAuthorityContentsAdminNo() +
                configMembership.getSelectAuthoritySuperAdminNo() +
                configMembership.getSelectAuthorityUserNo();
        System.out.println("authority : " + authoritiesLevel);
        if((authoritiesLevel & adminNo) == 0x71) return "ADMIN";
        else return "USER";
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
            System.out.println("profseq : " + membershipInfo.getProfileSeq());
            int authoritiesLevel = membershipInfo.getAuthoritiesLevel();
            writer.write(getAuthoritiesString(authoritiesLevel, configMembership));
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }


        clearAuthenticationAttributes(request);
    }

}
