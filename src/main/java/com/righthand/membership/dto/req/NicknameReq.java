package com.righthand.membership.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel(value = "닉네임 파라미터")
public class NicknameReq {

    @ApiParam("닉네임")
    @NotBlank
    String nickName;
}
