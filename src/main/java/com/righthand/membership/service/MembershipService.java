package com.righthand.membership.service;

import com.righthand.common.type.ReturnType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *  LoginService를 위한 IF Class
 *  Spring Security사용을 위해 UserDetailsService를 Override해야 한다.
 *
 *
 */
@Service
public interface MembershipService extends UserDetailsService {

    // 회원가입
    ReturnType signUp(Map input_data) throws  Exception;

    // 현재 로그인 한 유저의 세션 정보
    MembershipInfo currentSessionUserInfo() throws Exception;

    // password 암호화
    PasswordEncoder passwordEncoder();
}