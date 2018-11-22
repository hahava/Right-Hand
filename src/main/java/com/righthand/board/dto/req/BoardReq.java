package com.righthand.board.dto.req;

import com.righthand.common.board.Board;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "게시판 작성 파라미터")
public class BoardReq extends Board{
}
