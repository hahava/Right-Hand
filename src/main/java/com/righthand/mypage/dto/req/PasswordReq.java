package com.righthand.mypage.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel("패스워드 변경 파라미터")
public class PasswordReq {

    @NotBlank
    @ApiParam("새 비밀번호")
    private String newPwd;

    @NotBlank
    @ApiParam("새 비밀번호 중복확인")
    private String newPwdDup;
}
