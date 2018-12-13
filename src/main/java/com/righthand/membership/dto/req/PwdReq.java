package com.righthand.membership.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel(value = "패스워드 파라미터")
@Data
public class PwdReq {

    @ApiParam(value = "패스워드")
    @NotBlank
    private String userPwd;
}
