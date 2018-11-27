package com.righthand.mypage.controller;

import com.righthand.board.controller.BoardController;
import com.righthand.board.service.BoardService;
import com.righthand.common.GetClientProfile;
import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;
import com.righthand.common.util.ConvertUtil;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;
import com.righthand.mypage.dto.req.PasswordReq;
import com.righthand.mypage.dto.req.UserReq;
import com.righthand.mypage.service.TbUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
public class MypageController {

    private TbUserService tbUserService;
    private MembershipService membershipService;
    private BoardService boardService;

    @CacheEvict(value = "findUserAndProfileCache", key = "#{userSeq}")
    public void refreshCache(int userSeq){
        log.info("[Cache][Refresing] userSeq : " + userSeq);
    }

    @ApiOperation("내가 작성한 게시물")
    @GetMapping("/myBoard")
    public ResponseHandler<?> showBoardList(@ApiParam(value = "페이지 번호") @RequestParam int page) {
        final ResponseHandler<Object> result = new ResponseHandler<>();
        Map<String, Object> boardData = new HashMap<>();
        Map<String, Object> myBoard;

        try {
            MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
            if(membershipInfo == null) {
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            }
            else {
                boardData.put("authority", membershipInfo.getAuthoritiesLevel());
                boardData.put("nickname", membershipInfo.getNickname());
                try {
                    myBoard = boardService.getMyBoardList(membershipInfo.getProfileSeq(), page);
                    if(myBoard == null || myBoard.isEmpty()) {
                        boardData.put("total", 0);
                        boardData.put("data", null);
                        result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                        result.setData(boardData);
                        return result;
                    }
                    boardData.put("data", myBoard.get("data"));
                    boardData.put("total", myBoard.get("total"));
                    result.setData(boardData);
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                }
                catch (Exception e) {
                    result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                }
            }
        }
        catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
        }
        return result;
    }

    @ApiOperation("프로필 보기")
    @GetMapping("/profile")
    public ResponseHandler<?> showMyProfile() {
        final ResponseHandler<Object> result = new ResponseHandler<>();
        MembershipInfo membershipInfo;
        try {
            membershipInfo = membershipService.currentSessionUserInfo();
            if(membershipInfo == null) {
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            }
            else {
                try {
                    log.info("[GetProfile][Start]");
                    System.out.println("getUserSeq : " + membershipInfo.getUserSeq());
                    Map<String, Object> userInfo = tbUserService.findUserAndProfile(membershipInfo.getUserSeq());
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
    public ResponseHandler<?> editMyProfile(@ApiParam("사용자 정보") @Valid @RequestBody UserReq _params, BindingResult bindingResult){
        final ResponseHandler<Object> result = new ResponseHandler<>();
        if(!bindingResult.hasErrors()) {
            try {
                MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
                if (membershipInfo == null) {
                    result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
                } else {
                    Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
                    try {
                        tbUserService.updateUserProfile((String) params.get("nickname"), (String) params.get("tel"));
                        membershipInfo.setNickname(params.get("nickname").toString());
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
        result.setReturnCode(ReturnType.RTN_TYPE_MEMBERSHIP_DATA_INVALID_PATTERN_NG);
        return result;
    }

    @ApiOperation("비밀번호 변경")
    @PutMapping("/pwd")
    public ResponseHandler<?> editMyPassword(@ApiParam("비밀번호 정보") @Valid @RequestBody PasswordReq _params, BindingResult bindingResult){
        final ResponseHandler<Object> result = new ResponseHandler<>();
        if(!bindingResult.hasErrors()) {
            try {
                MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
                if (membershipInfo == null) {
                    result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
                } else {
                    Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);

                    String newPwd = (String) params.get("newPwd");
                    String newPwdDup = (String) params.get("newPwdDup");

                    // 공백 여부
                    if (newPwd.equals("") || newPwdDup.equals("") || newPwd == null || newPwdDup == null) {
                        result.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_EMPTY_NG);
                        return result;
                    }
                    if (newPwd.equals(newPwdDup)) {
                        try {
                            //트랜잭션 업데이트 PWD
                            result.setReturnCode(tbUserService.updateUserPwd(newPwd, newPwdDup, membershipInfo));
                        } catch (Exception e) {
                            log.error("[UpdateUserPwd][Exception]" + e.toString());
                        }
                    } else {
                        result.setReturnCode(ReturnType.RTN_TYPE_MEMBERSSHIP_PASSWORD_MATCH_NG);
                    }

                }
            } catch (Exception e) {
                log.error("[Session][Exception]" + e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            }
            return result;
        }
        result.setReturnCode(ReturnType.RTN_TYPE_NO_DATA);
        return result;
    }

}
