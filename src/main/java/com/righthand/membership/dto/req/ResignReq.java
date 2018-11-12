package com.righthand.membership.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiModel(value = "회원탈퇴 파라미터")
public class ResignReq {
    String reason;
}
