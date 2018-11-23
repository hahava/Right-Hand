package com.righthand.membership.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "이메일 중복 체크 파라미터")
public class UserIdReq implements Serializable {

    @ApiParam(value = "이메일", required = true)
    @NotBlank
    private String userId;
}
