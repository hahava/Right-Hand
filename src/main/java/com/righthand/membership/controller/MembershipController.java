package com.righthand.membership.controller;

import com.righthand.common.CheckData;
import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;
import com.righthand.common.util.ConvertUtil;
import com.righthand.membership.config.ConfigMembership;
import com.righthand.membership.dto.req.EmailReq;
import com.righthand.membership.dto.req.SignupReq;
import com.righthand.membership.dto.res.SessionRes;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value="/api/membership" )
public class MembershipController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigMembership configMembership ;

    @Autowired
    MembershipService membershipService;

    @Autowired
    CheckData checkData;

    /**
     * 회원 가입
     *
     *
     * @param : membership 설정값
     * @return : ReturnType
     */

    @ApiOperation(value="이메일 중복확인")
    @GetMapping(value = "/checkEmailDup")
    public ResponseHandler<?> checkEmailDup(final EmailReq _params){
        final ResponseHandler<?> result = new ResponseHandler<>();
        Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
        ReturnType rtn;

        try {
            rtn = membershipService
        }
    }

    @ApiOperation(value = "회원가입")
    @PostMapping(value="/signUp")
    public ResponseHandler<?> signUp(@Valid @RequestBody(required=false) final SignupReq _params) {
        final ResponseHandler<?> result = new ResponseHandler<>();
        Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
//        System.out.println(params.get("email"));
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
}
