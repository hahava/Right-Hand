package com.righthand.membership.controller;

import com.righthand.common.CheckData;
import com.righthand.common.PasswordHandler;
import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.email.EmailService;
import com.righthand.common.type.ReturnType;
import com.righthand.common.util.ConvertUtil;
import com.righthand.membership.config.ConfigMembership;
import com.righthand.membership.dao.MembershipDao;
import com.righthand.membership.dto.model.UserVO;
import com.righthand.membership.dto.req.*;
import com.righthand.membership.dto.res.UserIdRes;
import com.righthand.membership.dto.res.SessionRes;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import com.righthand.mypage.domain.profile.TbProfile;
import com.righthand.mypage.service.TbProfileService;
import com.righthand.mypage.service.TbUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/membership")
public class MembershipController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MembershipService membershipService;

    private final PasswordHandler passwordHandler;

    private final EmailService emailService;

    private final TbUserService tbUserService;

    private final TbProfileService tbProfileService;

    /**
     * 회원 가입
     *
     * @param : membership 설정값
     * @return : ReturnType
     */


    /*
     * 임시 비밀번호 10자리 발급 함수
     * */
    private String getRandomStr() {
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 7; i++) {
            temp.append((char) ((int) (rnd.nextInt(26)) + 97));
        }
        for (int i = 0; i < 2; i++) {
            temp.append((rnd.nextInt(10)));
        }
        temp.append((char) ((int) (rnd.nextInt(10)) + 33));

        return String.valueOf(temp);
    }


    @ApiOperation(value = "아이디 중복확인")
    @PostMapping(value = "/check/id/dup")
    public ResponseHandler<UserIdRes> checkIdDup(@Valid @RequestBody final UserIdReq _params,
                                                 BindingResult bindingResult) {
        final ResponseHandler<UserIdRes> result = new ResponseHandler<>();
        if(!bindingResult.hasErrors()) {
            Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
            ReturnType rtn;
            try {
                UserIdRes userIdRes = new UserIdRes();
                rtn = membershipService.checkUserIdDup(params);
                if (rtn.equals(ReturnType.RTN_TYPE_OK)) userIdRes.setIsExist(false);
                else userIdRes.setIsExist(true);
                result.setData(userIdRes);
                result.setReturnCode(rtn);
            } catch (Exception e) {
                logger.error("[ID DUP][Exception] " + e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_USERID_EXIST_NG);
            }
            return result;
        }
        result.setReturnCode(ReturnType.RTN_TYPE_NO_DATA);
        return result;
    }

    @ApiOperation(value = "회원가입")
    @PostMapping(value = "/signUp")
    public ResponseHandler<?> signUp(@Valid @RequestBody final SignupReq _params, BindingResult bindingResult) {
        final ResponseHandler<?> result = new ResponseHandler<>();
        if(bindingResult.hasErrors()){
            result.setReturnCode(ReturnType.RTN_TYPE_MEMBERSHIP_DATA_INVALID_PATTERN_NG);
            return result;
        }
        Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
        ReturnType rtn;
        try {
            rtn = membershipService.signUp(params);
            result.setReturnCode(rtn);
        } catch (Exception e) {
            logger.error("[SignUp][Exception] " + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_MEMBERSHIP_SIGNUP_NG);
        }

        return result;
    }

    @PostMapping(value = "/check/live/session")
    @ApiOperation(value = "checkLiveSession", notes = "세션이 열려있는지 확인")
    public ResponseHandler<Object> checkLiveSession() {
        ResponseHandler<Object> result = new ResponseHandler<>();

        try {

            MembershipInfo sessionInfo = membershipService.currentSessionUserInfo();
            logger.info(sessionInfo.getUsername());
            //1. 토큰 유효성 체크
            if (sessionInfo != null) {
                result.setReturnCode(ReturnType.RTN_TYPE_OK);
                SessionRes sessionRes = new SessionRes();
                sessionRes.setUserSeq(sessionInfo.getUserSeq());
                sessionRes.setAuthorityLevel(sessionInfo.getAuthoritiesLevel());
                sessionRes.setNickName(sessionInfo.getNickname());
                result.setData(sessionRes);

            } else {
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            }
        } catch (Exception e) {
            logger.error("[checkLiveSession][Exception] " + e.toString());
            Map<String, Object> session = new HashMap<>();
            session.put("authorityLevel", 0);
            result.setData(session);
            result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
        }

        return result;
    }

    @ApiOperation("회원탈퇴")
    @PutMapping("/resign")
    @CacheEvict(value = "findUserAndProfileCache", key = "#{userSeq}")
    public ResponseHandler<?> resign(@ApiParam("탈퇴사유") @Valid @RequestBody(required = false) final ResignReq _params,
                                     HttpServletRequest request) {
        final ResponseHandler<?> res = new ResponseHandler<>();
        Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
        ReturnType rtn;
        try {
            MembershipInfo sessionInfo = membershipService.currentSessionUserInfo();
            int userSeq = sessionInfo.getUserSeq();
            tbUserService.refreshCache(userSeq);
            params.put("userSeq", userSeq);
            rtn = membershipService.resign(params);
            //TODO 세션 파기
            request.getSession().invalidate();
            res.setReturnCode(rtn);
        } catch (Exception e) {
            logger.error("[Resign][Exception] " + e.toString());
            res.setReturnCode(ReturnType.RTN_TYPE_SESSION);
        }
        return res;
    }

    @ApiOperation("닉네임 중복확인")
    @PostMapping("/check/nick/dup")
    public ResponseHandler<?> checkNickDup(@Valid @RequestBody final NicknameReq _params, BindingResult bindingResult) {
        final ResponseHandler<?> res = new ResponseHandler<>();
        if(!bindingResult.hasErrors()) {
            Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
            ReturnType rtn;
            try {
                int count = membershipService.checkNickname(params);
                if (count == 0) {
                    res.setReturnCode(ReturnType.RTN_TYPE_OK);
                } else {
                    res.setReturnCode(ReturnType.RTN_TYPE_MEMBERSHIP_NICKNAME_EXIST_NG);
                }
            } catch (Exception e) {
                log.error("[checkNickname][Exception]" + e.toString());
                res.setReturnCode(ReturnType.RTN_TYPE_NG);
            }
            return res;
        }
        res.setReturnCode(ReturnType.RTN_TYPE_NO_DATA);
        return res;
    }

    @ApiOperation("비밀번호 중복확인")
    @PostMapping("/check/pwd/dup")
    public ResponseHandler<?> checkPwdDup(@Valid @RequestBody final PwdReq _params, BindingResult bindingResult) {
        final ResponseHandler<?> res = new ResponseHandler<>();
        if(!bindingResult.hasErrors()) {
            Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
            MembershipInfo membershipInfo = null;
            try {
                membershipInfo = membershipService.currentSessionUserInfo();
                if (membershipInfo == null) {
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
                if (passwordHandler.matches((CharSequence) params.get("userPwd"), userPwd)) {
                    res.setReturnCode(ReturnType.RTN_TYPE_OK);
                } else {
                    res.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_MATCH_NG);
                }
            } catch (Exception e) {
                log.error("[GetUserPwd][Exception]" + e.toString());
                res.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_NO_EXIST_NG);
            }
            return res;
        }
        res.setReturnCode(ReturnType.RTN_TYPE_NO_DATA);
        return res;
    }

    @ApiOperation("아이디 찾기")
    @GetMapping("/email")
    public ResponseHandler<?> findIdByEmail(@RequestParam final String userId) {
        final ResponseHandler<?> res = new ResponseHandler<>();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        boolean userIdExists = true;
        try {
            if (membershipService.checkUserIdDup(params) == ReturnType.RTN_TYPE_OK) userIdExists = false;
        } catch (Exception e) {
            log.error("[CheckUserIdDup][Exception]" + e.toString());
        }
        final String subject = "[Right Hand] ID 찾기 결과";
        String text = "<h1>안녕하세요.</h1> " +
                "<h1>Right Hand입니다.</h1>" +
                "<h2>ID 찾기에 대한 결과 안내드립니다.</h2>";
        if (userIdExists) {
            text += "현재, 조회하신 이메일로 가입된 계정이 있습니다.<br/>";
            text += "<a href = \"http://localhost:8080/api/membership/tempPwd?userId=" + userId + "\"/>임시 비밀번호 발급</a><br/>";
            text += "<a href = \"http://localhost:8080\"/>로그인</a>";
        } else {
            text += "현재, 조회하신 이메일로 가입된 계정이 존재하지 않습니다.<br/>";
            text += "<a href = \"http://localhost:8080\"/>Right Hand로 이동하기</a>";
        }
        emailService.sendMessage(userId, subject, text);
        res.setReturnCode(ReturnType.RTN_TYPE_OK);
        return res;
    }

    @ApiOperation("임시 비밀번호 발급")
    @GetMapping("/tempPwd")
    public void getTempPwd(@RequestParam String userId, HttpServletResponse response) throws IOException {
        final ResponseHandler<Object> res = new ResponseHandler<>();
        final String tempPwd = getRandomStr();
        final String encTempPwd = passwordHandler.encode(tempPwd);
        UserVO userVO = new UserVO();
        userVO.setUserId(userId);
        userVO.setPwd(encTempPwd);
        try {
            membershipService.changePwd(userVO);
        } catch (Exception e) {
            log.error("[ChangePwd][Exception]" + e.toString());
            res.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_CHANGE_NG);
        }
        final String subject = "[Right Hand] 임시 비밀번호 발급 관련";
        String text = "<h1>안녕하세요.</h1> " +
                "<h1>Right Hand입니다.</h1>" +
                "<h2>발급하신 임시 비밀번호입니다.</h2>" +
                "임시 비밀번호 : " + tempPwd + "<br/>" +
                "로그인 한 후 비밀번호를 바꾸어주세요. ";

        emailService.sendMessage(userId, subject, text);

        PrintWriter out;
        response.setContentType("text/html;charset=EUC-KR");
        out = response.getWriter();
        out.println("<Script language='JavaScript'>");
        out.println(" alert('임시 비밀번호가 발급되었습니다. 이메일을 확인해주세요.'); document.location.href='/'");
        out.println("</Script>");
        out.flush();
        out.close();
    }

    @ApiOperation("RH 코인과 리워드 파워 조회")
    @PostMapping("/coin")
    public ResponseHandler<?> showRhCoinAndRewardPower(){
        final ResponseHandler<Object> res = new ResponseHandler<>();
        MembershipInfo membershipInfo = null;
        try {
            membershipInfo = membershipService.currentSessionUserInfo();
            if(membershipInfo == null) throw new Exception();
        } catch (Exception e) {
            log.error("[Session][Exception] : ", e.toString());
            res.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            return res;
        }
        long profileSeq = membershipInfo.getProfileSeq();
        TbProfile tbProfile = tbProfileService.getOne(profileSeq);
        Map<String, Object> coinInfo = new HashMap<>();
        coinInfo.put("rhCoin", tbProfile.getRhCoin());
        coinInfo.put("rewardPower", tbProfile.getRewardPower());
        res.setReturnCode(ReturnType.RTN_TYPE_OK);
        res.setData(coinInfo);
        return res;
    }

}