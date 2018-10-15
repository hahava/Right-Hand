package com.righthand.membership.customHandler;

import com.righthand.common.type.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 세션에 저장된 리퀘스트들을 가져온다.
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        logger.info("Login Success Handler");

        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();

        writer.write(ReturnType.RTN_TYPE_OK.getStrValue() );
        writer.flush();

        clearAuthenticationAttributes(request);
    }

}
