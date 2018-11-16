package com.righthand.membership.dto.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "닉네임")
public class NicknameReq {
    String nickName;
}
