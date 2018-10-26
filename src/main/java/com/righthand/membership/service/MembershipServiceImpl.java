package com.righthand.membership.service;

import com.righthand.common.CheckData;
import com.righthand.common.GetNowTime;
import com.righthand.common.PasswordHandler;
import com.righthand.common.VaildationCheck.ConfigValidationCheck;
import com.righthand.common.type.ReturnType;
import com.righthand.membership.config.ConfigMembership;
import com.righthand.membership.dao.MembershipDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Semaphore;


/**
 *
 * 회원정보 처리를 위한 클래스
 * 로그인 - Spring Security form login 기능 사용
 * 회원가입 - REST Api 방식 사용
 * 세션 정보 확인 , 계정 권한 등 회원정보를 위한 기본적인 기능을 제공 한다.
 *
 */
@Service
public class MembershipServiceImpl implements MembershipService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MembershipDao membershipDao;

    @Autowired
    ConfigMembership configMembership;

    @Autowired
    PasswordHandler passwordHandler;

    @Autowired
    ConfigValidationCheck configValidationCheck;

    @Autowired
    CheckData checkData;

    @Autowired
    GetNowTime getNowTime;

    static Semaphore signUpSemaphore = new Semaphore(1);

    /**
     * DB field가 이미 존재하는 지 확인
     * user id, 나 email등이 이미 존재 하는지 확인 할 수 있다.
     *
     * @param params
     * @return ReturnType
     */
    public boolean checkExistInUser(Map<String, Object> params) throws  Exception{

        // user 데이터에서 찾기
        Map resMemberData = membershipDao.selectUser(params);

        if ((resMemberData == null) || resMemberData.isEmpty()) {
            ;
        } else {
            // If already exist.
            if (resMemberData.size() > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param
     * @return
     * @throws Exception
     */
    public ReturnType signUp(Map input_data) throws Exception{

        String userId = null;
        String pwd = null;

        HashMap<String, Object> params = new HashMap<>();

        logger.info("[Service][SignUp]");

        // 로직을 다른 누군가 실행 중일 때는 대기 .
        signUpSemaphore.acquire();

        try {

            ///////////////////////////////////////////////////////////////////////////////////////////////
            // 1. User id 검증
            ///////////////////////////////////////////////////////////////////////////////////////////////
            userId = (String) input_data.get("userId");

            params.put("userId", userId);

            if (this.checkExistInUser(params) == true) {
                signUpSemaphore.release();
                return ReturnType.RTN_TYPE_MEMBERSSHIP_USERID_EXIST_NG;
            }

            // 별도의 로그인 타입이 설정 되어 있지 않으면, 기본 호빈 이메일 로그인 타입으로 설정 한다.
            if((checkData.isExist(input_data.get("loginType")) == false ) ||
                    ( Integer.parseInt(input_data.get("loginType").toString())== configMembership.getLoginTypeEmail()) ) {
                input_data.put("loginType", configMembership.getLoginTypeEmail());
                input_data.put("email", userId);
            }
//            System.out.println("email : " + input_data.get("email"));
            // 2) email의 pattern
            if (configValidationCheck.checkEmail((String) input_data.get("email")) != 0) {
                signUpSemaphore.release();
                return ReturnType.RTN_TYPE_MEMBERSSHIP_EMAIL_PATTERN_NG;
            }

            logger.info("[Service][SignUp] USER ID OK");
            ///////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////////////
            // 2. Password 검증 및 암호화
            ///////////////////////////////////////////////////////////////////////////////////////////////
            pwd = (String) input_data.get("userPwd");

            // 2) Id의 pattern
            if(configValidationCheck.checkPwd(pwd) != 0)
            {
                signUpSemaphore.release();
                return ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_PATTERN_NG;
            }

            // 3) 암호화
            String encPassword = passwordHandler.encode(pwd);

            if (!passwordHandler.matches(pwd, encPassword)) {
                logger.error("[Service][SignUp]PWD ENC ERR");
                signUpSemaphore.release();
                return ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_ENC_NG;
            }

            input_data.remove("userPwd");

            input_data.put("userPwd", encPassword);

            logger.info("[Service][SignUp] USER PWD OK");
            ///////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////////////
            // 3. 디폴트 설정
            ///////////////////////////////////////////////////////////////////////////////////////////////
            // authority level
            input_data.put("authority", configMembership.getSelectAuthorityLevelDefault());


            // 활동 알림 받기 설정이 별도로 설저되지 않을 경우 '예'로 설정.
            if(checkData.isExist(input_data.get("actAlertYn")) == false ) {
                input_data.put("actAlertYn", "Y");
            }

            // 공사 알림 동의 설정이 별도로 없을 경우 '예'로 설정.
            if(checkData.isExist(input_data.get("workingAlertYn")) == false ) {
                input_data.put("workingAlertYn", "Y");
            }

            ///////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////////////
            // 4. 데이터 저장
            ///////////////////////////////////////////////////////////////////////////////////////////////
            membershipDao.insertUser(input_data);

            if(input_data.get("lang") != null) {
                String settingLang = input_data.get("lang").toString();
                if(settingLang.contains("en")) {
                    input_data.put("lang", "002");
                } else if (settingLang.contains("ko")) {
                    input_data.put("lang", "003");
                } else {
                    input_data.put("lang", configMembership.getDefaultLangCode());
                }
            }
            else {
                input_data.put("lang", configMembership.getDefaultLangCode());
            }

            membershipDao.insertProfile(input_data);



            ///////////////////////////////////////////////////////////////////////////////////////////////
        }
        catch (Exception e) {
            signUpSemaphore.release();
            throw new Exception(e);
        }

        signUpSemaphore.release();
        return ReturnType.RTN_TYPE_OK;
    }

    /*
    public ReturnType signUp(SignupReq signupReq) throws Exception{

        String userId = null;
        String pwd = null;

        HashMap<String, Object> params = new HashMap<>();

        logger.info("[Service][SignUp]");

        // 로직을 다른 누군가 실행 중일 때는 대기 .
        signUpSemaphore.acquire();

        try {

            ///////////////////////////////////////////////////////////////////////////////////////////////
            // 1. User id 검증
            ///////////////////////////////////////////////////////////////////////////////////////////////
            params.put("userId", signupReq.getUserId());

            // 이미 동일한 유저 아이디가 있을 때
            if (this.checkExistInUser(params) == true) {
                signUpSemaphore.release();
                return ReturnType.RTN_TYPE_MEMBERSSHIP_USERID_EXIST_NG;
            }

            // 2) email의 pattern
            if (configValidationCheck.checkEmail(signupReq.getEmail()) != 0) {
                signUpSemaphore.release();
                return ReturnType.RTN_TYPE_MEMBERSSHIP_EMAIL_PATTERN_NG;
            }

            logger.info("[Service][SignUp] USER ID OK");
            ///////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////////////
            // 2. Password 검증 및 암호화
            ///////////////////////////////////////////////////////////////////////////////////////////////
            // 2) Id의 pattern
            if(configValidationCheck.checkPwd(signupReq.getUserPwd()) != 0)
            {
                signUpSemaphore.release();
                return ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_PATTERN_NG;
            }

            // 3) 암호화
            String encPassword = passwordHandler.encode(pwd);

            if (!passwordHandler.matches(pwd, encPassword)) {
                logger.error("[Service][SignUp]PWD ENC ERR");
                signUpSemaphore.release();
                return ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_ENC_NG;
            }

            signupReq.setUserPwd(encPassword);

            logger.info("[Service][SignUp] USER PWD OK");
            ///////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////////////
            // 3. 디폴트 설정
            ///////////////////////////////////////////////////////////////////////////////////////////////
            // authority level
            signupReq.setAuthority(configMembership.getSelectAuthorityLevelDefault());

            ///////////////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////////////
            // 4. 데이터 저장
            ///////////////////////////////////////////////////////////////////////////////////////////////
            membershipDao.insertUser(signupReq);
            membershipDao.insertProfile(signupReq);
        }
        catch (Exception e) {
            signUpSemaphore.release();
            throw new Exception(e);
        }

        signUpSemaphore.release();
        return ReturnType.RTN_TYPE_OK;
    }
*/

    /**
     * Spring Security 정보 setup 함수.
     *
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String userId = username;

        Map MembershipData = new HashMap();
        MembershipData.put("userId", username);

        Map tempUser = membershipDao.selectUser(MembershipData);

        MembershipInfo member = new MembershipInfo();

        // 해당 유저가 존재 하지 않을 때
        if (!checkData.isExist(tempUser)) {
            throw new UsernameNotFoundException("member info empty");
        }

        member.setPassword(tempUser.get("USER_PWD").toString());
        member.setUsername(tempUser.get("USER_ID").toString());


        if(checkData.isNotEmpty(tempUser.get("PENDING_YN")) && tempUser.get("PENDING_YN").toString().equals("Y")) {
            member.setAccountNonLocked(false);
        }
        else {
            member.setAccountNonLocked(true);
        }

        // Spring Security 에러 처리 부분
        member.setAccountNonExpired(true);
        member.setCredentialsNonExpired(true);
        member.setCredentialsNonExpired(true);
        member.setEnabled(true);

        member.setLoginTime(getNowTime.getTimeByDate());

        logger.info("[Service][LogIn]");
        logger.info(member.getUsername() + "/"  + member.getName());

        // 권한 저장
        member.setAuthorities(getAuthorities(Integer.parseInt(tempUser.get("AUTHORITY").toString())));
        member.setAuthoritiesLevel(Integer.parseInt(tempUser.get("AUTHORITY").toString()));

        // 유저 시퀀스 저장
        member.setUserSeq(Integer.parseInt(tempUser.get("USER_SEQ").toString()));

        return member;
    }

    /**
     * 사용자의 권한을 설정 한다.
     * Session context에 담길 user 권한을 String Array 형태로 저장 한다.
     *
     * @param authorityLevel : User 권한 값
     * @return authorities : 권한 Array 값.
     */
    public Collection<GrantedAuthority> getAuthorities(int authorityLevel) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        logger.info("authorityLevel " + authorityLevel);


        // Check : unauthority
        if (authorityLevel == configMembership.getSelectUnauthorityUserNo()) {
            authorities.add(new SimpleGrantedAuthority(configMembership.getSelectUnauthorityUserStr()));
        }
        else {

            // Check : General User authority
            if ((authorityLevel & configMembership.getSelectAuthorityUserNo()) > 0) {
                authorities.add(new SimpleGrantedAuthority(configMembership.getSelectAuthorityUserStr()));
            }

            // Check : Contents Admin authority
            if ((authorityLevel & configMembership.getSelectAuthorityContentsAdminNo()) > 0) {
                authorities.add(new SimpleGrantedAuthority(configMembership.getSelectAuthorityContentsAdminStr()));
            }

            // Check : General Admin authority
            if ((authorityLevel & configMembership.getSelectAuthorityAdminNo()) > 0) {
                authorities.add(new SimpleGrantedAuthority(configMembership.getSelectAuthorityAdminStr()));
            }

            // Check : Super Admin authority
            if ((authorityLevel & configMembership.getSelectAuthoritySuperAdminNo()) > 0) {
                authorities.add(new SimpleGrantedAuthority(configMembership.getSelectAuthoritySuperAdminStr()));
            }
        }

        return authorities;
    }

    /**
     * @return 세션에서 user 정보를 가지고 온다.
     */
    public MembershipInfo currentSessionUserInfo() throws Exception {

        if(!checkData.isExist(SecurityContextHolder.getContext())) {
            return null;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!checkData.isExist(authentication)) {
            return null;
        }

        if(authentication.getPrincipal() == null) {
            return null;
        }

        if(authentication.getPrincipal().equals("anonymousUser") ||
                (authentication.getPrincipal() == null)) {
            return null;
        }

        MembershipInfo user = (MembershipInfo) authentication.getPrincipal();

        return user;
    }

    /**
     * Spring Security 암호화 함수 Override
     *
     * @return
     */
    @Override
    public PasswordEncoder passwordEncoder() {
        return passwordHandler.getPasswordEncoder();
    }

}

