package com.righthand.board.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "게시판 검색 파라미터")
public class BoardReq implements Serializable {
    @ApiParam(value = "검색어", required = true)
    @NotNull
    private String searchedWord;
}
