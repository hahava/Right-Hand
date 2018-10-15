package com.righthand.membership.customHandler;

import com.righthand.membership.service.MembershipService;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *  로그인 실패 핸들러
 *
 *  로그인이 실패한 이유를 확인하고 , 그에 맞는 에러 정보를 리턴 한다.
 *
 */
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    MembershipService membershipService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg)
            throws IOException, ServletException {

        PrintWriter writer = response.getWriter();

        // 400 error
        response.setStatus(HttpStatus.SC_BAD_REQUEST);
        writer.flush();
    }
}
