package com.righthand.membership.dto.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

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

    @ApiModelProperty(value = "로그인 타입")
    private String loginType;

    @ApiParam(value = "비밀번호", format = "password")
    private String userPwd; //  userPwd

    @ApiModelProperty(value = "이름(닉네임)")
    @NotNull
    private String nickName; //  userName

    @ApiModelProperty(value = "성별")
    @NotNull
    private String gender; //  gender

    @ApiModelProperty(value = "생년")
    @NotNull
    private int birthYear; //

    @ApiModelProperty(value = "자녀여부")
    @NotNull
    private String childrenYn; //

    @ApiModelProperty(value = "마켓팅 동의 여부")
    private String marketingAdvYn;

    @ApiModelProperty(value = "서드파티 이미지 패스")
    private String profileImgPath;

    @ApiModelProperty(value = "언어")
    private String lang;

    private int authority; //

}
