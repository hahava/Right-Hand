package com.righthand.mypage.controller;

import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;
import com.righthand.common.util.ConvertUtil;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import com.righthand.mypage.dto.req.UserReq;
import com.righthand.mypage.service.TbUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class MypageController {

    private TbUserService tbUserService;
    private MembershipService membershipService;

    @ApiOperation("프로필 보기")
    @GetMapping("/profile")
    public ResponseHandler<?> showMyProfile() {
        final ResponseHandler<Object> result = new ResponseHandler<>();

        try {
            if(membershipService.currentSessionUserInfo() == null) {
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            }
            else {
                try {
                    log.info("[GetProfile][Start]");
                    Map<String, Object> userInfo = tbUserService.findUserAndProfile();
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                    result.setData(userInfo);
                } catch (Exception e) {
                    System.out.println("[GetProfile][Exception] " + e.toString());
                    result.setReturnCode(ReturnType.RTN_TYPE_MYPAGE_PROFILE_NG);
                }
            }
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            log.error("[currentSession][error] " + e.toString());
        }

        return result;
    }

    @ApiOperation("프로필 수정")
    @PutMapping("/profile")
    public ResponseHandler<?> editMyProfile(@ApiParam("사용자 정보") @Valid @RequestBody UserReq _params){
        final ResponseHandler<Object> result = new ResponseHandler<>();
        try {
            MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
            if(membershipInfo == null) {
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            }
            else {
                Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
                try {
                    tbUserService.updateUserProfile((String)params.get("nickname"), (String)params.get("tel"));
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                } catch (Exception e) {
                    System.out.println("[EditProfile][Exception] " + e.toString());
                    result.setReturnCode(ReturnType.RTN_TYPE_MYPAGE_EDIT_PRO_NG);
                }
            }
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
        }

        return result;
    }
}
