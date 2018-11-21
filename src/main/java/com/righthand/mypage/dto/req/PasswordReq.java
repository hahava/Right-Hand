package com.righthand.mypage.dto.req;

import lombok.Data;

@Data
public class PasswordReq {
    private String newPwd;
    private String newPwdDup;
}
