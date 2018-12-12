package com.righthand.membership.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel(value = "전화번호 파라미터")
public class TelReq {

    @ApiParam(value = "전화번호")
    @NotBlank
    private String tel;
}
