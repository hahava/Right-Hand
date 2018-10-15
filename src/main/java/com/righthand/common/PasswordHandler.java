package com.righthand.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 패스워드 암호화 및 비교를 위한 클래스
 * 패스워드를 암호화 한다. 또한 암호화 된 문자와 입력된 패스워드를 비교 한다.
 * 인코딩 방식 : BCrypt
 *
 * @author: Raymond
 * @date: 2018-03-12
 *
 */
@Service
public class PasswordHandler implements PasswordEncoder{

    private PasswordEncoder passwordEncoder;

    public PasswordHandler() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public PasswordHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * password를 암호화 한다.
     *
     * @param rawPassword 암호화 대상 문자열
     * @return 암호화 된 문자열
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 비밀번호를 비교 한다.
     *
     * @param rawPassword
     * @param encodedPassword
     * @return true = 동일 비번, false = 다른 비번
     */
    @Override public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     *  makeRandomKey
     *  숫자, 영문자로 이루어진 랜덤키 생성 함수
     *
     * @param sizeOfData
     * @return
     */
    public String makeRandomKey(int sizeOfData) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, sizeOfData);
    }
}
