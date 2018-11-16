package com.righthand.membership.controller;

import com.righthand.common.CheckData;
import com.righthand.common.PasswordHandler;
import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;
import com.righthand.common.util.ConvertUtil;
import com.righthand.membership.config.ConfigMembership;
import com.righthand.membership.dto.req.*;
import com.righthand.membership.dto.res.UserIdRes;
import com.righthand.membership.dto.res.SessionRes;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value="/api/membership" )
public class MembershipController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigMembership configMembership;

    @Autowired
    MembershipService membershipService;

    @Autowired
    PasswordHandler passwordHandler;

    @Autowired
    CheckData checkData;

    /**
     * 회원 가입
     *
     *
     * @param : membership 설정값
     * @return : ReturnType
     */

    @ApiOperation(value="아이디 중복확인")
    @PostMapping(value = "/check/id/dup")
    public ResponseHandler<UserIdRes> checkIdDup(@Valid @RequestBody final UserIdReq _params){
        final ResponseHandler<UserIdRes> result = new ResponseHandler<>();
        Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
        ReturnType rtn;
        try{
            UserIdRes userIdRes = new UserIdRes();
            rtn = membershipService.canUseEmail(params);
            if(rtn.equals(ReturnType.RTN_TYPE_OK)) userIdRes.setIsExist(false);
            else userIdRes.setIsExist(true);
            result.setData(userIdRes);
            result.setReturnCode(rtn);
        } catch(Exception e) {
            logger.error("[EmailDUP][Exception] " + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }
        return  result;
    }

    @ApiOperation(value = "회원가입")
    @PostMapping(value="/signUp")
    public ResponseHandler<?> signUp(@Valid @RequestBody(required=false) final SignupReq _params) {
        final ResponseHandler<?> result = new ResponseHandler<>();
        Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
        ReturnType rtn;

        try{
            rtn = membershipService.signUp(params);
            result.setReturnCode(rtn);
        } catch(Exception e) {
            logger.error("[SignUp][Exception] " + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }

        return  result;
    }

    @PostMapping(value="/check/live/session")
    @ApiOperation(value="checkLiveSession", notes="세션이 열려있는지 확인")
    public ResponseHandler<SessionRes> checkLiveSession() {

        ResponseHandler<SessionRes> result = new ResponseHandler<>() ;

        try {

            MembershipInfo sessionInfo = membershipService.currentSessionUserInfo();
            logger.info(sessionInfo.getUsername());
            //1. 토큰 유효성 체크
            if(sessionInfo != null) {
                result.setReturnCode(ReturnType.RTN_TYPE_OK);
                SessionRes sessionRes = new SessionRes();
                sessionRes.setUserSeq(sessionInfo.getUserSeq());
                sessionRes.setAuthorityLevel(sessionInfo.getAuthoritiesLevel());
                result.setData(sessionRes);

            } else {
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            }
        }
        catch(Exception e) {
            logger.error("[checkLiveSession][Exception] " + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }

        return result;
    }

    @ApiOperation("회원탈퇴")
    @PutMapping("/resign")
    public ResponseHandler<?> resign(@ApiParam("탈퇴사유") @Valid @RequestBody(required=false) final ResignReq _params) {
        final ResponseHandler<?> res = new ResponseHandler<>();
        Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
        ReturnType rtn;
        try{
            MembershipInfo sessionInfo = membershipService.currentSessionUserInfo();
            int userSeq = sessionInfo.getUserSeq();
            params.put("userSeq", userSeq);
            rtn = membershipService.resign(params);
            res.setReturnCode(rtn);
        } catch(Exception e) {
            logger.error("[Resign][Exception] " + e.toString());
            res.setReturnCode(ReturnType.RTN_TYPE_NG);
        }

        return  res;
    }

    // 세라가 변경해야함!
    @ApiOperation("닉네임 중복확인")
    @PostMapping("/check/nick/dup")
    public ResponseHandler<?>  checkNickDup(@Valid @RequestBody final NicknameReq _params){
        final ResponseHandler<?> res = new ResponseHandler<>();
        Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
        ReturnType rtn;
        try {
            int count = membershipService.checkNickname(params);
            if(count == 0){
                res.setReturnCode(ReturnType.RTN_TYPE_OK);
            }else{
                res.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_USERID_EXIST_NG);
            }
        } catch (Exception e) {
            log.error("[checkNickname][Exception]" + e.toString());
            res.setReturnCode(ReturnType.RTN_TYPE_NG);
        }
        return res;
    }

    @ApiOperation("비밀번호 중복확인")
    @PostMapping("/check/pwd/dup")
    public ResponseHandler<?>  checkPwdDup(@Valid @RequestBody final PwdReq _params){
        final ResponseHandler<?> res = new ResponseHandler<>();
        Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
        MembershipInfo membershipInfo = null;
        try {
            membershipInfo = membershipService.currentSessionUserInfo();
            if(membershipInfo == null){
                log.error("[SessionNoExist][Exception]");
                res.setReturnCode(ReturnType.RTN_TYPE_SESSION);
                return res;
            }
        } catch (Exception e) {
            log.error("[GetSessionUserInfo][Exception]" + e.toString());
            res.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            return res;
        }

        try {
            String userPwd = membershipService.getUserPwd(membershipInfo.getUserSeq());
            if(passwordHandler.matches((CharSequence) params.get("userPwd"), userPwd)){
                res.setReturnCode(ReturnType.RTN_TYPE_OK);
            }else{
                res.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_MATCH_NG);
            }
        } catch (Exception e) {
            log.error("[GetUserPwd][Exception]" + e.toString());
            res.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_NO_EXIST_NG);
        }
        return res;
    }
}
