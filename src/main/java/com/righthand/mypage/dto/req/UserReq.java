package com.righthand.mypage.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;

@Data
@ApiModel(value = "회원정보 파라미터")
public class UserReq {

    @ApiParam(value = "닉네임", required = true)
    @NotBlank
//    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ_]*$")
    private String nickname;

    @ApiParam(value = "전화번호", required = true)
    @NotBlank
    @Pattern(regexp = "^[0-9\\-_]*$")
    private String tel;
}
