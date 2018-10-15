package com.righthand.membership.customHandler;

import com.righthand.common.type.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *  권한이 없는 페이지 접속 처리 함수
 *
 *  권한이 없는 자가 권한이 주어진 페이지에 접근 하거나,
 *  인증받지 않은 자가 로그인을 했을 때, 상황에 맞는 적절한 페이지로 강제 리다이렉트 시킨다.
 *
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException, ServletException {

        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.error("[AccessDeniedHandler] User: " + auth.getName()
                        + " attempted to access the protected URL: "
                        + request.getRequestURI());
        }

        logger.error("[AccessDeniedHandler] Auth"+auth.getAuthorities().toString());

        // 403 error
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        PrintWriter writer = response.getWriter();

        writer.write(ReturnType.RTN_TYPE_MEMBERSSHIP_AUTHORITY_NG.getStrValue());
        writer.flush();
    }
}
