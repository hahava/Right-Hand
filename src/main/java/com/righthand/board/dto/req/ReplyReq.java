package com.righthand.board.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("댓글 파라미터")
public class ReplyReq {

    @ApiParam(value = "게시물 번호", required = true)
    @NotNull
    private int boardSeq;

    @ApiParam(value = "댓글 내용", required = true)
    @NotBlank
    private String content;

    @ApiParam(value = "코인")
    private Double reqCoin;
}
