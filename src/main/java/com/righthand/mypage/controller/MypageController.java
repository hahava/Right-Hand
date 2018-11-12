package com.righthand.mypage.controller;

import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;
import com.righthand.mypage.service.TbUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class MypageController {

    TbUserService tbUserService;

    @GetMapping("/mypage")
    public ResponseHandler<?> showMyPage(){
        final ResponseHandler<Object> result = new ResponseHandler<>();
        try {
            Map<String, Object> userInfo = tbUserService.findUserAndProfile();
            result.setReturnCode(ReturnType.RTN_TYPE_OK);
            result.setData(userInfo);
        } catch (Exception e) {
            result.setReturnCode(ReturnType.RTN_TYPE_NG);
        }
        return result;
    }
}
