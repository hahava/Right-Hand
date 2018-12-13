package com.righthand.membership.customHandler;

import com.righthand.common.type.ReturnType;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class CustomLogoutHandler implements LogoutSuccessHandler {

    @CacheEvict(value = "findUserAndProfileCache", key = "#{userSeq}")
    public void refreshCache(int userSeq) {
        log.info("[Cache][Refresing] userSeq : " + userSeq);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        try {
            int userSeq = Integer.parseInt(request.getParameter("userSeq"));
            refreshCache(userSeq);
            log.info("Logout Success Handler");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = response.getWriter();
            writer.write(ReturnType.RTN_TYPE_OK.getStrValue());
            writer.flush();
        }catch (NumberFormatException e){
            log.error("[Parameter][NumberFormatException]" + e.toString());
        }
    }
}
