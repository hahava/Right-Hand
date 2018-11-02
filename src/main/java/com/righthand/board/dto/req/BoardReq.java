package com.righthand.board.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "게시판 작성 파라미터")
public class BoardReq implements Serializable {

    @ApiParam(value = "제목", required = true)
    @NotNull
    private String boardTitle;

    @ApiParam(value = "내용", required = true)
    @NotNull
    private String boardContent;
}
