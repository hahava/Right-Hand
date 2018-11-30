package com.righthand.membership.dto.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;



@Data
@ApiModel(value = "회원가입 파라미터")
public class SignupReq implements Serializable {

    @ApiParam(value = "아이디", required = true)
    @NotNull
    private String userId; //  userId

    @ApiModelProperty(value = "이메일")
    private String email;

    @ApiParam(value = "비밀번호", format = "password", required = true)
    @NotNull
    private String userPwd; //  userPwd

    @ApiModelProperty(value = "이름(닉네임)", required = true)
    @NotNull
    private String nickName; //  userName

    @ApiModelProperty(value = "성별", required = true)
    @NotNull
    private String gender; //  gender

    @ApiModelProperty(value = "생년", required = true)
    @NotNull
    private int birthYear;

    @ApiModelProperty(value = "이름", required = true)
    @NotNull
    private String userName;

    @ApiModelProperty(value = "핸드폰번호", required = true)
    @NotNull
    private String tel;

    @ApiModelProperty(value = "추천인 아이디")
    private String recommender;

}
