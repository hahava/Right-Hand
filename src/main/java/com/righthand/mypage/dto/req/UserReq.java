package com.righthand.mypage.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
@ApiModel(value = "회원정보 파라미터")
public class UserReq {

    @ApiParam(value = "닉네임", required = true)
    private String nickname;

    @ApiParam(value = "전화번호", required = true)
    private String tel;
}
