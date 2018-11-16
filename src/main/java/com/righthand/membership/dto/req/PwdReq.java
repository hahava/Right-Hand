package com.righthand.membership.dto.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "패스워드")
@Data
public class PwdReq {
    private String userPwd;
}
