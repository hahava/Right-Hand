package com.righthand.common.board;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;

@Data
public class Board implements Serializable {

    @ApiParam(value = "제목", required = true)
    protected String boardTitle;

    @ApiParam(value = "내용", required = true)
    protected String boardContent;
}
